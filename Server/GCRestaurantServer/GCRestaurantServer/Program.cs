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
        static void Main(string[] args)
        {
            Server server = new Server(1231);
            server.Connect += Server_Connect;
            server.Receive += Server_Receive_Try;
            server.Exit += Server_Exit;

           
            while (true)
            {
                System.Threading.Thread.Sleep(4000);
            }
        }

        private static void Server_Connect(ESocket socket)
        {
            Console.WriteLine("로그인");
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
        }
    }
}
