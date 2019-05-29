using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GCRestaurantServer;
using EasyMysql;
using System.IO;
using LogSystem;
namespace GCRestaurantServer.UnitTest
{
    [TestClass]
    public class LogSystemTest
    {
        static LogSystem.LogSystem logSystem;
        [TestMethod]
        public void LogSystemCreate()
        {
            logSystem = new LogSystem.LogSystem();
        }

        [TestMethod]
        public void AddLogTest()
        {
            System.Threading.Thread.Sleep(400);
            for (int i = -5; i < 5; i++)
            {
                logSystem.AddLog(i, "1", "2");
                logSystem.AddLog(i, "1", "2", true);
                logSystem.AddLog(new Log("1", "2", 10));
            }
        }
        [TestMethod]
        public void CreateLog()
        {
            new Log("1", "2", 10).ToString();
            new Log("1", "2", 10).ToString(DateTime.Now);
        }
    }
}