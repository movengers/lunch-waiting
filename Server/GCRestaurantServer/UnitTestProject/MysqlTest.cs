using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GCRestaurantServer;
using EasyMysql;
using System.IO;
namespace GCRestaurantServer.UnitTest
{
    [TestClass]
    public class MysqlTest
    {
        MysqlOption option => Program.mysqlOption;

        [TestMethod]
        public void ConfigTest()
        {
            if ("Read File" == option.Host)
            {
                Assert.Fail("Config 파일을 찾을 수 없습니다.");
            }
            if ("db_name" == option.Database)
            {
                Assert.Fail("Config 에 DB 정보가 입력되지 않았습니다.");
            }
        }
        [TestMethod]
        public void ReadUserDB()
        {
            TableTest("user");
        }
        [TestMethod]
        public void ReadRestaurantDB()
        {
            TableTest("restaurant");
        }
        private void TableTest(string table)
        {
            try
            {
                MysqlNode node = new MysqlNode(option, "SELECT * FROM " + table);
                using (node.ExecuteReader()) ;
            }
            catch (Exception e)
            {
                Assert.Fail(e.Message);
            }
        }
    }
}
