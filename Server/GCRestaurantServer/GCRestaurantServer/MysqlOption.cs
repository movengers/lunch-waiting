using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.IO;
namespace GCRestaurantServer
{
    public class MysqlOption : EasyMysql.MysqlOption
    {
        public MysqlOption(string host, string database, string id, string password, string option = null) : base(host, database, id, password, option)
        {
            if (host == "Read File")
            {
                LoadFile();
            }
        }

        public void LoadFile()
        {
            try
            {
                string data = File.ReadAllText("MysqlConfig.json");
                JObject json = JObject.Parse(data);
                Host = (string)json["host"];
                Database = (string)json["database"];
                ID = (string)json["id"];
                Password = (string)json["password"];
                Option = (string)json["option"];
                Program.LogSystem.AddLog(3, "MysqlOption", "설정 파일 로드 성공");
            }
            catch (FileNotFoundException e)
            {

            }
            catch (Exception e)
            {
                throw e;
            }

        }
    }
}
