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
namespace GCRestaurantServer.UnitTest
{
    [TestClass]
    public class CrawlingTest
    {
        [TestMethod]
        public void UrlQueryParserTest()
        {
            Assert.AreEqual(ParseSupport.UrlQueryParser("https://test.com/?id=1321&asd=342")["id"], "1321");
            Assert.AreEqual(ParseSupport.UrlQueryParser("https://test.com/?id=1321&asd=342")["asd"], "342");

            Assert.AreEqual(ParseSupport.UrlQueryParser("https://?id=1231&asds/?id=1321&asd=342")["id"], "1321");
        }

        [TestMethod]
        public void UTF8CharTest()
        {
            Assert.AreEqual(ParseSupport.UTF8Char("https://test.com/?id=1321&asd=342"), "https://test.com/?id=1321&asd=342");
            Assert.AreEqual(ParseSupport.UTF8Char("https:\\u002f\\u002ftest.com/?id=1321&asd=342"), "https://test.com/?id=1321&asd=342");
            Assert.AreEqual(ParseSupport.UTF8Char("https://test.com/?id=1321&asd=342"), "https://test.com/?id=1321&asd=342"); ;
        }

        [TestMethod]
        public void CrawlingErrorDetectTest()
        {
            // 빈 값을 입력했을때 오류
            Assert.AreEqual(ParseSupport.Crawling(null), null);

            Assert.AreEqual(ParseSupport.Crawling(""), null);
            Assert.AreEqual(ParseSupport.Crawling("ftp://aeawe"), null);
        }

        [TestClass]
        public class Restaurant
        {
            [TestMethod]
            public void GetPlaceIDTest()
            {
                Assert.AreEqual(NaverAPIModule.GetPlaceID(null), -1);
                Assert.AreEqual(NaverAPIModule.GetPlaceID(""), -1);
                Assert.AreEqual(NaverAPIModule.GetPlaceID("태평 돈가스"), 628782093);
            }
            [TestMethod]
            public void GetPlaceDescTest()
            {
                Assert.AreNotEqual(NaverAPIModule.GetPlaceDescription(628782093), null);
            }
            [TestMethod]
            public void GetPlaceCrawling()
            {
                //Assert.AreEqual(NaverAPIModule.GetInfomationDetail(0), null);
                Assert.AreNotEqual(NaverAPIModule.GetInfomationDetail(628782093), null);
            }
            [TestMethod]
            public void Handler()
            {
                GCRestaurantServer.Module.Handler.Restaurant.GetID("태평 돈가스");
                GCRestaurantServer.Module.Handler.Restaurant.Infomation(628782093);
                GCRestaurantServer.Module.Handler.Restaurant.WaitingList();
            }

        }
    }
}
