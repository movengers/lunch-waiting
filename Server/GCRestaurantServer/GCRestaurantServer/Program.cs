using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
using Newtonsoft.Json.Linq;
namespace GCRestaurantServer
{
    class Program
    {
        public static Dictionary<ESocket, OnlineUser> users = new Dictionary<ESocket, OnlineUser>();
        static void Main(string[] args)
        {
            Server server = new Server(1231);
            server.Connect += Server_Connect;
            server.Receive += Server_Receive_Try;
            server.Exit += Server_Exit;
         
           
            while (true)
            {
                System.Threading.Thread.Sleep(4000);
                JObject json = new JObject();
                json["type"] = 1;
                lock(users)
                {
                    foreach(OnlineUser user in users.Values)
                    {
                        user.socket.Send(json);
                    }
                }
            }
        }

        private static void Server_Connect(ESocket socket)
        {
            Console.WriteLine("로그인");
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
            Console.WriteLine(Message.ToString());
            switch ((int)Message["type"])
            {
                case 1000:
                    Console.WriteLine("안드로이드 테스트 메세지 : " + (string)Message["message"]);
                    break;
            }
        }



        private static void Server_Exit(ESocket socket)
        {
            Console.WriteLine("누군가 종료함");
            lock (users)
            {
                users.Remove(socket);
            }
        }
    }
}
