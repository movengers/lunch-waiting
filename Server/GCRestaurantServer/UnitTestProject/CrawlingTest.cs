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
        public void CrawlingTestUnit()
        {
            HtmlAgilityPack.HtmlDocument dom = ParseSupport.Crawling("");
            Assert.AreEqual(dom, null);

        }
    }
}
