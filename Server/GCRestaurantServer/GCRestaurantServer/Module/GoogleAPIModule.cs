using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.IO;
using System.Web;
using Newtonsoft.Json.Linq;
namespace GCRestaurantServer
{
    public static class GoogleAPIModule
    {
        public static Object Geocoding(string address)
        {
            HttpWebRequest hreq = (HttpWebRequest)WebRequest.Create("https://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&address=" + HttpUtility.HtmlEncode(address) + "&key=" + ConfigManagement.GetObject("google_api")["key"]);
            hreq.Method = "GET";
            hreq.ContentType = "plain/text;charset=utf-8";
            HttpWebResponse hres = (HttpWebResponse)hreq.GetResponse();
            if (hres.StatusCode == HttpStatusCode.OK)
            {
                Stream dataStream = hres.GetResponseStream();
                StreamReader sr = new StreamReader(dataStream, Encoding.UTF8);
                string result = sr.ReadToEnd();
                dataStream.Close();
                sr.Close();
                JObject result_json = (JObject)JObject.Parse(result)["result"]["geometry"]["location"];
                return result_json;
            }
            return null;
        }
    }
}
