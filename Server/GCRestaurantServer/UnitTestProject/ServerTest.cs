using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GCRestaurantServer;
using EasyMysql;
using System.IO;
using NetworkLibrary;
using Newtonsoft.Json.Linq;

namespace GCRestaurantServer.UnitTest
{
    [TestClass]
    public class ServerTest
    {
        int count = 0;
        static Client client = null;
        [TestMethod]
        public void ServerOpen()
        {
            Server server = new Server(1231);
            SocketEvent.Receive p = delegate (ESocket socket, JObject json)
               {
                   count++;
               };
            server.Receive += p;
        }

        [TestMethod]
        public void ConnectTest()
        {
            System.Threading.Thread.Sleep(500);
            client = new Client("127.0.0.1", 1231);
            client.Start();
        }
        [TestMethod]
        public void ClientMessageTest()
        {
            System.Threading.Thread.Sleep(1000);
            for (int i = 0; i< 100; i++)
              client.Send(new JObject());
            
        }
    }
}
