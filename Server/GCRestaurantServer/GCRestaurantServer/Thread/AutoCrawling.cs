using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.Text.RegularExpressions;
using EasyMysql;
namespace GCRestaurantServer
{
    class AutoCrawling
    {
        private static String[] keywords = new string[] { "가천대 맛집", "가천대 식당"};
        public static void main()
        {
            Program.LogSystem.AddLog(2, "AutoCrawling", "네이버를 통해 음식점 데이터를 갱신합니다");

            foreach (string keyword in keywords)
            {
                Program.LogSystem.AddLog(1, "AutoCrawling", keyword + " 키워드로 검색 시작");
                JObject search_data = NaverAPIModule.SearchPlace((string)ConfigManagement.GetObject("naver_api")["client_id"], 
                    (string)ConfigManagement.GetObject("naver_api")["client_secret"],
                    keyword);
                foreach (JObject restaurant in search_data["items"])
                {

                    MysqlNode update = new MysqlNode(Program.mysqlOption, "INSERT INTO restaurant (no, title, roadAddress, mapx, mapy, category, image) VALUES (?no, ?title, ?roadAddress, ?mapx, ?mapy, ?category, ?image)");
                    update["title"] = Regex.Replace((string)restaurant["title"], "(<[/a-zA-Z]+>)", "");
                    update["roadAddress"] = (string)restaurant["roadAddress"];
                    update["mapx"] = (string)restaurant["mapx"];
                    update["mapy"] = (string)restaurant["mapy"];
                    update["category"] = (string)restaurant["category"];

                    update["no"] = NaverAPIModule.GetPlaceID((string)update["title"], (string)update["roadAddress"]);
                    update["image"] = NaverAPIModule.GetPlaceImage((int)update["no"]);

                    update.ExecuteNonQuery();
                    Program.LogSystem.AddLog(1, "AutoCrawling", update["title"] + " 를 리스트에 등록");

                    // 메뉴 갱신

                    JArray menus = NaverAPIModule.GetPlaceMenu((int)update["no"]);
                    foreach (JObject json in menus)
                    {
                        MysqlNode menu_update = new MysqlNode(Program.mysqlOption, "INSERT INTO menu (restaurant_no, priority, name, price, description, image) VALUES (?restaurant_no, ?priority, ?name, ?price, ?description, ?image)");
                        menu_update["restaurant_no"] = update["no"];
                        menu_update["priority"] = (string)json["priority"];
                        menu_update["name"] = (string)json["name"];
                        menu_update["price"] = (string)json["price"];

                        if (String.IsNullOrEmpty((string)json["desc"]))
                            menu_update["description"] = null;
                        else
                            menu_update["description"] = (string)json["desc"];

                        if (((JArray)json["images"]).Count > 0)
                            menu_update["image"] = (string)json["images"][0];
                        else
                            menu_update["image"] = null;

                        menu_update.ExecuteNonQuery();
                        Program.LogSystem.AddLog(1, "AutoCrawling", json["name"] + " 를 메뉴 리스트에 등록");
                    }
                }
            }
            Program.LogSystem.AddLog(1, "AutoCrawling", "ra");
           
            int id = NaverAPIModule.GetPlaceID("원조 태평동 곱창");
            NaverAPIModule.GetPlaceMenu(id);

            string description = NaverAPIModule.GetPlaceDescription(id);
            JObject jsoan = NaverAPIModule.GetInfomationDetail(id);
        }
    }
}
