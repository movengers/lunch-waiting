using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LogSystem
{
    /// <summary>
    /// 여러 종류의 Log를 효율적으로 관리할 수 있는 시스템입니다.
    /// </summary>
    public class LogSystem
    {
        List<Log> datalist = new List<Log>();
        /// <summary>
        /// 로그 시스템의 시작시간입니다. 별도의 지정이 없으면 자동 설정됩니다.
        /// </summary>
        public DateTime StartTime;
        public delegate void UserPacketEvent(object sender, Log data);
        public event UserPacketEvent on_LogPrint;
        /// <summary>
        /// 콘솔에 로그를 출력할지 결정합니다.
        /// </summary>
        public bool ConsoleWrite = true;
        /// <summary>
        /// 로그를 출력할때 이 수준 이상의 로그는 확인하지 않습니다.
        /// </summary>
        public int ViewLevel = -1;
        private void LogPrint(Log data)
        {
            if (data.level >= ViewLevel)
            {
                if (ConsoleWrite) Console.WriteLine(data.ToString(StartTime));
                on_LogPrint?.Invoke(this, data);
            }
        }
        public LogSystem()
        {
            StartTime = DateTime.Now;
        }

        /// <summary>
        /// 로그를 기록합니다.
        /// </summary>
        /// <param name="level"> -1(패킷), 0(디버그), 1(시스템 세부 로그 - 매우 상세함), 2 (시스템 작동 로그 - 자동 수행되는 시스템),  3(시스템 작동 로그), 4(사용자 액션)</param>
        /// <param name="sender"></param>
        /// <param name="data"></param>
        /// <param name="error"></param>
        public void AddLog(int level, Object sender, string data, bool error = false)
        {
            Log log = new Log()
            {
                sender = sender,
                level = level,
                data = data,
                logtime = DateTime.Now,
                Error = error
            };
            LogPrint(log);
        }
        public void AddLog(Log data)
        {
            LogPrint(data);
        }
    }
}
