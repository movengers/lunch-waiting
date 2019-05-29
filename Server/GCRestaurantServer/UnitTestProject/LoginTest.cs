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
    public class LoginTest
    {
        [TestMethod]
        public void LogSystemCreate()
        {
            OnlineUser user = new OnlineUser(null);
        }
        [TestMethod]
        public void LoginTestMethod()
        {
            OnlineUser user = new OnlineUser(null);
            try
            {
                user.Login("");
                bool a = user.IsLogin;
                string v = user.img;
                string c = user.name;


                Assert.Fail("토큰이 없지만 로그인 실패 메세지 없음");
            }
            catch(Exception e)
            {

            }
        }
        [TestMethod]
        public void MessageSendTestMethod()
        {
            OnlineUser user = new OnlineUser(null);
            try
            {
                user.Message("");
                Assert.Fail("연결 소켓이 없지만 메세지 전송 에러 없음");
            }
            catch (Exception e)
            {

            }
        }

    }
}