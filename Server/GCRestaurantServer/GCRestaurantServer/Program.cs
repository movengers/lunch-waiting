using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using Newtonsoft.Json.Linq;
using System.Threading;
namespace GCRestaurantServer
{
    public static class Program
    {

        public static LogSystem.LogSystem LogSystem = new LogSystem.LogSystem();
        public static MysqlOption mysqlOption = new MysqlOption("Read File", "", "", "");
        public static Dictionary<ESocket, OnlineUser> users = new Dictionary<ESocket, OnlineUser>();
        static void Main(string[] args)
        {
            LogSystem.ViewLevel = 0;
            Server server = new Server(1231);
            server.Connect += Server_Connect;
            server.Receive += Server_Receive_Try;
            server.Exit += Server_Exit;

            LogSystem.AddLog(3, "Program", "서버가 실행되었습니다.");
            new Thread(AutoCrawling.main).Start();
            new Thread(AutoWaitingComputing.main).Start();
            while (true)
            {
                System.Threading.Thread.Sleep(4000);
                JObject json = new JObject();
                json["type"] = 1;
                lock(users)
                {
                    foreach(OnlineUser user in users.Values)
                    {
                        user.Send(json);
                    }
                }
            }
        }

        private static void Server_Connect(ESocket socket)
        {
            LogSystem.AddLog(-1, "Program", "새로운 소켓이 연결되었습니다.");
            lock (users)
            {
                users.Add(socket, new OnlineUser(socket));
            }
        }
        private static void Server_Receive_Try(ESocket socket, JObject Message)
        {
            try
            {
                Server_Receive(socket, Message);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message + "\r\n" + e.StackTrace);
            }
        }
        private static void Server_Receive(ESocket socket, JObject Message)
        {
            OnlineUser user = users[socket];
            LogSystem.AddLog(-1, "Program - Receive", Message.ToString());
            switch ((int)Message["type"])
            {
                case PacketType.Login:
                    KakaoModule.KakaoLogin(user, (string)Message["token"]);
                    break;
                case PacketType.Debug:
                    LogSystem.AddLog(0, "Program", (string)Message["message"]);
                    break;
                case PacketType.RestaurantInfo:
                    if (Message["no"] == null)
                        user.Message("음식점 고유 번호값이 존재하지 않습니다.");
                    else
                    {
                        JObject json = Module.Handler.Restaurant.Infomation((int)Message["no"]);
                        if (json == null)
                            user.Message("해당하는 번호의 음식점이 없습니다.");
                        else
                            user.Send(json);
                    }
                    break;

                case PacketType.RestaurantWaitingList:
                    user.Send(Module.Handler.Restaurant.WaitingList());
                    break;
                case PacketType.GetRestaurantID:
                    user.Send(Module.Handler.Restaurant.GetID((string)Message["title"]));
                    break;
                case PacketType.PositionUpdate:
                    user.position = new Position(Message);
                    break;
                case PacketType.ClickLikes:
                    Module.Handler.Restaurant.Likes(user, (int)Message["no"], (bool)Message["positive"]);
                    break;
                case PacketType.GetLikes:
                    user.Send(Module.Handler.Restaurant.StateLikes(user, (int)Message["no"]));
                    break;
                case PacketType.RestaurantRankingList:
                    string[] data;
                    JArray array = (JArray)Message["categories"];
                    data = array.ToObject<List<string>>().ToArray();
                    user.Send(Module.Handler.Restaurant.RankingList(data));
                    break;

            }
        }



        private static void Server_Exit(ESocket socket)
        {
            LogSystem.AddLog(-1, "Program", "기존 소켓의 연결이 해제되었습니다.");
            lock (users)
            {
                users.Remove(socket);
            }
        }
    }
}
