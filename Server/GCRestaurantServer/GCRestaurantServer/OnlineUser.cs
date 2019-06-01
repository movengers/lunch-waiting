using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using EasyMysql;
using Newtonsoft.Json.Linq;
namespace GCRestaurantServer
{
    public class OnlineUser
    {
        public static CacheList<int> receive_waiting_user = new CacheList<int>(3600);
        public int id = 0;
        public bool IsLogin {
            get {
                return id > 0;
            }
        }
        public string name { get; private set; }
        public string img { get; private set; }
        public Position position = null;
        private ESocket socket;
        public OnlineUser(ESocket socket)
        {
            this.socket = socket;
        }
        public void Send(JObject json)
        {
            Program.LogSystem.AddLog(-1, "Program - Send", json.ToString());
            socket.Send(json);
        }
        public static void Send(int id, JObject json)
        {
            foreach (OnlineUser user in Program.users.Values)
            {
                if (user.id == id) user.Send(json);
            }
        }
        public static void SendAll(JObject json)
        {
            lock(Program.users)
            {
                foreach (OnlineUser user in Program.users.Values)
                {
                    user.Send(json);
                }
            }
        }
        public void Login(string token)
        {
            Program.LogSystem.AddLog(4, "LoginModule", "토큰을 통한 로그인 시도 " + token);

            JObject message = new JObject();
            message["type"] = PacketType.Login;
            if (String.IsNullOrEmpty(token))
            {
                throw new Exception("토큰이 존재하지 않습니다.");
            }
            else
            {
                JObject data = KakaoModule.GetUserInformation(token);
                if (data != null)
                {
                    id = (int)data["id"];
                    name = (string)data["properties"]["nickname"];
                    img = (string)data["properties"]["thumbnail_image"];
                    MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT * FROM user WHERE id = ?id");
                    node["id"] = id;
                    using (node.ExecuteReader())
                    {
                        MysqlNode update = new MysqlNode(Program.mysqlOption, "SQL");

                        if (node.Read())
                            update.ChangeSql("UPDATE user SET name=?name WHERE id = ?id");
                        else
                            update.ChangeSql("INSERT INTO user (id,name) VALUES (?id, ?name)");

                        update["id"] = id;
                        update["name"] = name;
                        update.ExecuteNonQuery();
                    }
                }
                else
                {
                    throw new Exception("유효하지 않은 토큰입니다.");
                }
            }
        }
        public void Message(string message)
        {
            JObject json = new JObject();
            json["type"] = PacketType.Message;
            json["message"] = message;
            socket.Send(json);
        }
        public void Notify(string tag, int no, string title, string content)
        {
            JObject json = new JObject();
            json["type"] = PacketType.Notify;
            json["no"] = no;
            json["tag"] = tag;
            json["title"] = title;
            json["content"] = content;
            socket.Send(json);
        }
    }
}
