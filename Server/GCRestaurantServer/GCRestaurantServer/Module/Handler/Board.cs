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
    public static class Board
    {
        public static JObject GetList(int no = -1)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM board_writer WHERE parent_no IS NULL ORDER BY no DESC");
            if (no > 0)
            {
                node.ChangeSql("SELECT * FROM board_writer WHERE no = ?no ORDER BY no DESC");
                node["no"] = no;
            }

            JObject json = new JObject();
            json["type"] = PacketType.ReadBoard;
            JArray list = new JArray();
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    JObject item = new JObject();
                    item["no"] = node.GetInt("no");
                    item["content"] = node.GetString("content");
                    item["time"] = node.GetString("time");
                    item["user_id"] = node.GetInt("user_id");
                    item["name"] = node.GetString("name");
                    list.Add(item);
                }
            }
            json["list"] = list;
            return json;
        }

        public static bool WriteArticle(OnlineUser user, string content, int parent_no = 0)
        {
            if (string.IsNullOrEmpty(content))
            {
                user.Message("내용을 입력해주세요.");
                return false;
            }
            MysqlNode menu_update = new MysqlNode(Program.mysqlOption, "INSERT INTO board (user_id, content, parent_no) VALUES (?user_id, ?content, ?parent_no)");
            menu_update["user_id"] = user.id;
            menu_update["content"] = content;
            if (parent_no == 0)
                menu_update["parent_no"] = null;
            else
                menu_update["parent_no"] = parent_no;
            long result = menu_update.ExecuteInsertQuery();
            if (result > 0)
            {
                if (parent_no == 0)
                {
                    JObject json = new JObject();
                    json["type"] = PacketType.WriteBoardItem;
                    json["item"] = ((JArray)GetList((int)result)["list"])[0];
                    OnlineUser.SendAll(json);
                }
                else
                {
                    user.Send(GetCommentList(parent_no));
                    List<int> RelatedUser = GetRelatedUser(parent_no);
                    foreach (int id in RelatedUser)
                    {
                        if (id != user.id) // 올린 사람은 제외
                            OnlineUser.Notify(id, "comment", (int)result, "내가 올린 게시글에 답변이 등록되었습니다.", "("+ user.name+") "+ content);
                    }

                }
                user.Message("등록에 성공했습니다.");
                return true;
            }
            else
            {
                user.Message("등록에 실패했습니다.");
                return false;
            }
        }
        public static JObject GetCommentList(int no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM board_writer WHERE parent_no = ?no");
            node["no"] = no;
            JObject json = new JObject();
            json["type"] = PacketType.ReadComments;
            json["no"] = no;
            JArray list = new JArray();
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    JObject item = new JObject();
                    item["no"] = node.GetInt("no");
                    item["content"] = node.GetString("content");
                    item["time"] = node.GetString("time");
                    item["user_id"] = node.GetInt("user_id");
                    item["name"] = node.GetString("name");
                    list.Add(item);
                }
            }
            json["list"] = list;
            return json;
        }
        public static List<int> GetRelatedUser(int no)
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM board WHERE parent_no = ?no or no = ?no");
            node["no"] = no;
            List<int> list = new List<int>();
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    list.Add(node.GetInt("user_id"));
                }
            }
            return list;
        }
    }
}
