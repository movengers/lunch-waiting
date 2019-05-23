using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
namespace LogSystem
{
    public class Log
    {
        /// <summary>
        /// -1(패킷), 0(디버그), 1(시스템 세부 로그 - 매우 상세함), 2 (시스템 작동 로그 - 자동 수행되는 시스템),  3(시스템 작동 로그), 4(사용자 액션)
        /// </summary>
        public int level = 3;
        public DateTime logtime;
        public Object sender;
        public string data
        {
            get
            {
                return org_data;
            }
            set
            {
                org_data = value.Replace("\r\n", "\\r\\n");
                org_data = org_data.Replace("\n", "\\n");
            }
        }
        private string org_data = null;
        public bool Error = false;
        public Log()
        {
        }
        public Log(Object sender, string data, int level)
        {
            logtime = DateTime.Now;
            this.sender = sender;
            this.data = data;
            this.level = level;
        }
        public string level_ToString()
        {
            switch (level)
            {
                case -1:
                    return "패킷       ";
                case 0:
                    return "디버그     ";
                case 1:
                    return "세부 로그  ";
                case 2:
                    return "시스템 로그";
                case 3:
                    return "알림       ";
                case 4:
                    return "일반       ";
            }
            return "";
        }
        public string ToString(DateTime StartTime)
        {
            TimeSpan time = (logtime - StartTime);
            return String.Format("{0:0.000} - {3} [{1}] {2}", time.TotalSeconds, centeredString().PadRight(20), data, level_ToString());
           // return base.ToString();
        }
        public override string ToString()
        {
            return String.Format("{0} - {3} [{1}] {2}", logtime.ToString(), centeredString().PadRight(20), data, level_ToString());
        }
        private string centeredString()
        {
            string sender_string = sender.ToString() + (Error ? " - ERROR" : "");
            var totalLength = 30;
            var centeredString =
                 sender_string.PadLeft(((totalLength - sender_string.Length) / 2)
                                        + sender_string.Length)
                               .PadRight(totalLength);
            return centeredString;
        }
    }
}
