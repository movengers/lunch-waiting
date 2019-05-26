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
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM waiting_view");

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
                        item["time"] = node.GetString("waiting");
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
