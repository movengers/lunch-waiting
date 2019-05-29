using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GCRestaurantServer;
using EasyMysql;
using System.IO;
using GCRestaurantServer.Module;
using GCRestaurantServer.Module.Handler;
using GCRestaurantServer;
namespace GCRestaurantServer.UnitTest
{
    [TestClass]
    public class GCWaiting
    {
        [TestMethod]
        public void ComputeWait()
        {
            AutoWaitingComputing.UnitTest = true;
            GCRestaurantServer.AutoWaitingComputing.main();
        }
    }
}
