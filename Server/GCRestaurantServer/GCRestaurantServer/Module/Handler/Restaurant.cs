using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using Newtonsoft.Json.Linq;
using EasyMysql;

namespace GCRestaurantServer.Module.Handler
{
    public static class Restaurant
    {
        private static Dictionary<int, CacheList<int>> get_waiting_queue = new Dictionary<int, CacheList<int>>();
        public static void AddWaitingListener(OnlineUser user, int no)
        {
            if (!get_waiting_queue.ContainsKey(no)) get_waiting_queue[no] = new CacheList<int>(1800);
            get_waiting_queue[no].Add(user.id);
            // 결과 전송
            user.Send(GetContainsWaitingListener(user, no));
            user.Message("30분간 이 음식점의 대기열 정보를 직접 수신합니다.");

            // 다른 유저들에게 메세지 전송
            JObject json = new JObject();
            json["type"] = PacketType.RequestWaitingToUser;
            json["no"] = no;
            json["title"] = GetTitle(no);

            Position position = GetPosition(no);
            foreach (OnlineUser other_user in Program.users.Values)
            {
                if (other_user.position != null && other_user.position.DistanceToMeter(position) < 256)
                {
                    if (!OnlineUser.receive_waiting_user.Contains(other_user.id))
                        other_user.Send(json);
                }
            }
        }
        public static List<int> GetWaitingListener(int no)
        {
            if (get_waiting_queue.ContainsKey(no))
                return get_waiting_queue[no].Values;
            else
                return new List<int>();
        }
        public static bool ContainsWaitingListener(OnlineUser user, int no)
        {
            if (get_waiting_queue.ContainsKey(no))
            {
                return get_waiting_queue[no].Contains(user.id);
            }
            return false;
        }
        public static JObject GetContainsWaitingListener(OnlineUser user, int no)
        {
            JObject json = new JObject();
            json["type"] = PacketType.ContainsWaitingListener;
            json["no"] = no;
            json["contains"] = ContainsWaitingListener(user, no);
            return json;
        }
        public static void UpdateWaiting(OnlineUser user, int no, int time) // 0~1~2
        {
            // 수신 금지 목록에 등록
            OnlineUser.receive_waiting_user.Add(user.id);
            user.Message("소중한 정보 감사합니다.");

            MysqlNode node = new MysqlNode(Program.mysqlOption, "INSERT INTO waiting_data (restaurant_no, user_id, waiting) VALUES (?restaurant_no, ?user_id, ?waiting)");
            node["restaurant_no"] = no;
            node["user_id"] = user.id;
            if (time < 2)
                node["waiting"] = time * 5;
            else
                node["waiting"] = 15;
            node.ExecuteNonQuery();
            int? result = AutoWaitingComputing.Update(no);
            foreach(int id in GetWaitingListener(no))
            {
                OnlineUser.Notify(id, "waiting", no, GetTitle(no) + " 대기 시간 수신", "예상 시간 : " + result.Value + "분");
            }
        }
        public static JObject GetID(string title)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant WHERE title = ?title");
            node["title"] = title;

            using (node.ExecuteReader())
            {
                if (node.Read())
                {
                    JObject json = new JObject();
                    json["type"] = PacketType.GetRestaurantID;
                    json["no"] = node.GetInt("no");
                    return json;
                }
            }
            return null;
        }
        public static string GetTitle(int no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant WHERE no = ?no");
            node["no"] = no;

            using (node.ExecuteReader())
            {
                if (node.Read())
                {
                    return node.GetString("title");
                }
            }
            return null;
        }
        public static Position GetPosition(int no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant WHERE no = ?no");
            node["no"] = no;
            using (node.ExecuteReader())
            {
                if (node.Read())
                {
                    Position position = new Position(node.GetDouble("mapy"), node.GetDouble("mapx"));
                    return position;
                }
            }
            return null;
        }
        public static JObject Infomation(int id)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant WHERE no = ?no");
            node["no"] = id;

            using (node.ExecuteReader())
            {
                if (node.Read())
                {

                    JObject json = new JObject();
                    json["type"] = PacketType.RestaurantInfo;
                    json["title"] = node.GetString("title");
                    json["category"] = node.GetString("category");
                    json["description"] = node.GetString("description");
                    json["address"] = node.GetString("roadAddress");
                    json["image"] = node.GetString("image");

                    JArray menus = new JArray();
                    MysqlNode menu_node = new MysqlNode(Program.mysqlOption, "SELECT * FROM menu WHERE restaurant_no = ?no ORDER BY priority");
                    menu_node["no"] = id;
                    using (menu_node.ExecuteReader())
                    {
                        while(menu_node.Read())
                        {
                            JObject menu = new JObject();
                            menu["name"] = menu_node.GetString("name");
                            menu["price"] = menu_node.GetString("price");
                            menu["description"] = menu_node.GetString("description");
                            menu["image"] = menu_node.GetString("image");
                            menus.Add(menu);
                        }
                    }
                    json["menus"] = menus;
                    return json;
                }
                else
                {
                    return null;
                }
            }
        }
        public static JObject WaitingList()
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant");

            JObject json = new JObject();
            json["type"] = PacketType.RestaurantWaitingList;
            JArray list = new JArray();
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    if (!node.IsNull("mapx"))
                    {
                        JObject item = new JObject();
                        item["no"] = node.GetInt("no");
                        item["title"] = node.GetString("title");
                        item["time"] = node.GetString("computed_waiting");
                        item["x"] = node.GetDouble("mapx");
                        item["y"] = node.GetDouble("mapy");
                        list.Add(item);
                    }
                }
            }

            json["list"] = list;
            return json;
        }
        public static JObject RankingList(String category)
        {
            JObject json = new JObject();
            json["type"] = PacketType.RestaurantRankingList;
            json["category"] = category;
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM restaurant join rest_likes on restaurant.no=rest_likes.no WHERE category REGEXP (?data) ORDER BY likes desc");
            node["data"] = category.Replace(",","|");
            JArray list = new JArray();
            json["list"] = list;
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    JObject item = new JObject();
                    item["no"] = node.GetInt("no");
                    item["title"] = node.GetString("title");
                    item["time"] = node.GetString("computed_waiting");
                    item["likes"] = node.GetString("likes");
                    item["category"] = node.GetString("category");

                    list.Add(item);
                }
            }
            return json;

        }
        public static void Likes(OnlineUser user, int restaurant_no, bool positive)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SQL");

            if (positive)
                node.ChangeSql("INSERT INTO rest_likes_data (restaurant_no, user_id) VALUES (?restaurant_no, ?user_id)");
            else
                node.ChangeSql("DELETE FROM rest_likes_data where restaurant_no = ?restaurant_no AND user_id = ?user_id");
            node["restaurant_no"] = restaurant_no;
            node["user_id"] = user.id;
            int result = node.ExecuteNonQuery();
            if (result > 0)
            {
                user.Send(StateLikes(user,restaurant_no));
            }
            else
            {
                user.Message("요청이 실패했습니다.");
            }
        }

        public static int GetLikes(int restaurant_no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM rest_likes WHERE no = ?no");
            node["no"] = restaurant_no;
            using (node.ExecuteReader())
            {
                if (node.Read())
                {
                    return node.GetInt("likes");
                }
            }
            return 0;
        }
        public static bool ClickedLikes(OnlineUser user, int restaurant_no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM rest_likes_data WHERE user_id = ?id and restaurant_no = ?no");
            node["no"] = restaurant_no;
            node["id"] = user.id;
            using (node.ExecuteReader())
            {
                if (node.Read())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        public static JObject StateLikes(OnlineUser user, int restaurant_no)
        {
            JObject json = new JObject();
            json["type"] = PacketType.GetLikes;
            json["no"] = restaurant_no;
            json["positive"] = ClickedLikes(user, restaurant_no);
            json["likes"] = GetLikes(restaurant_no);
            return json;
        }
    }
}
