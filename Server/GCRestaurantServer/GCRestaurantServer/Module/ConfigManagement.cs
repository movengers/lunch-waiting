using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.IO;

namespace GCRestaurantServer
{
    public class ConfigManagement
    {
        private static JObject original_object = null;
        private static void Init()
        {
            if (original_object == null)
            {
                string data = File.ReadAllText("Config.json");
                original_object = (JObject)JObject.Parse(data)["config"];
            }
        }

        public static JObject GetObject(string name)
        {
            Init();
            return (JObject)original_object[name];
        }
    }
}
