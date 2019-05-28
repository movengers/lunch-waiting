using System;
using GCRestaurantServer;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UnitTestProject
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void TestMethod1()
        {
         
            GCRestaurantServer.Module.Handler.Restaurant.GetID("0");
            Assert.AreEqual("A", "B");
        }
    }
}
