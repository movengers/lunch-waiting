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
                    other_user.Send(json);
                }
            }
        }
        public static List<int> GetWaitingListener(int no)
        {
            if (!get_waiting_queue.ContainsKey(no))
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
    }
}
