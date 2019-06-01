using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using System.Net;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using Newtonsoft.Json.Linq;
using EasyMysql;

namespace GCRestaurantServer
{
    class KakaoModule
    {
        public static void KakaoLogin(OnlineUser user, string token)
        {
            JObject message = new JObject();
            message["type"] = PacketType.Login;
            try
            {
                user.Login(token);

                message["result"] = true;
                message["name"] = user.name;
                message["icon"] = user.img;
                message["id"] = user.id;
                message["message"] = "success";
            }
            catch (Exception e)
            {
                message["result"] = false;
                message["message"] = e.Message;
            }
            user.Send(message);
        }
        public static int CheckToken(string token)
        {
            try
            {
                HttpWebRequest hreq = (HttpWebRequest)WebRequest.Create("https://kapi.kakao.com/v1/user/access_token_info");
                hreq.Method = "GET";
                hreq.ContentType = "application/x-www-form-urlencoded;charset=utf-8";
                hreq.Headers["Authorization"] = "Bearer " + token;
                HttpWebResponse hres = (HttpWebResponse)hreq.GetResponse();
                if (hres.StatusCode == HttpStatusCode.OK)
                {
                    Stream dataStream = hres.GetResponseStream();
                    StreamReader sr = new StreamReader(dataStream, Encoding.UTF8);
                    string result = sr.ReadToEnd();
                    dataStream.Close();
                    sr.Close();
                    JObject json = JObject.Parse(result);
                    return (int)json["id"];
                }
            }
            catch (WebException e)
            {
                if (e.Message.IndexOf("400") >= 0)
                    return -400;
            }
            catch (Exception e)
            {
                Program.LogSystem.AddLog(3, "LoginModule", "CheckToken: " + e.Message, true);
            }
            return 0;
        }
        public static JObject GetUserInformation(string token)
        {
            try
            {
                HttpWebRequest hreq = (HttpWebRequest)WebRequest.Create("https://kapi.kakao.com/v2/user/me");
                hreq.Method = "GET";
                hreq.ContentType = "application/x-www-form-urlencoded;charset=utf-8";
                hreq.Headers["Authorization"] = "Bearer " + token;
                HttpWebResponse hres = (HttpWebResponse)hreq.GetResponse();
                if (hres.StatusCode == HttpStatusCode.OK)
                {
                    Stream dataStream = hres.GetResponseStream();
                    StreamReader sr = new StreamReader(dataStream, Encoding.UTF8);
                    string result = sr.ReadToEnd();
                    dataStream.Close();
                    sr.Close();
                    return JObject.Parse(result);
                }
            }
            catch (WebException e)
            {
                if (e.Message.IndexOf("400") >= 0)
                    return null;
            }
            catch (Exception e)
            {
                Program.LogSystem.AddLog(3, "LoginModule", "GetUserInformation: " + e.Message, true);
            }
            return null;
        }
    }
}
