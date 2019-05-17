using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using Newtonsoft.Json.Linq;

namespace GCRestaurantServer
{
    class LoginModule
    {
        public static void KakaoLogin(OnlineUser user, string token)
        {
            Program.LogSystem.AddLog(4, "LoginModule", "토큰을 통한 로그인 시도 " + token);

            JObject message = new JObject();
            message["type"] = PacketType.Login;
            if (String.IsNullOrEmpty(token))
            {
                message["result"] = false;
                message["message"] = "no token";
                user.socket.Send(message);
                return;
            }
            else
            {
                user.login = true;
                message["result"] = true;
                message["message"] = "success";
                user.socket.Send(message);
                return;
            }
        }
    }
}
