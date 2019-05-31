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
        public static JObject GetList()
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM board_writer WHERE parent_no IS NULL ORDER BY no DESC");

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
                    item["name"] = node.GetString("name");
                    list.Add(item);
                }
            }
            json["list"] = list;
            return json;
        }
    }
}
