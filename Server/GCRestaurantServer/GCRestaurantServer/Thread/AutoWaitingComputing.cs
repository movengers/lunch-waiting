using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using EasyMysql;
using NetworkLibrary;
using Newtonsoft.Json.Linq;
using System.Threading;
namespace GCRestaurantServer
{
    class AutoWaitingComputing
    {
        public static void main()
        {
            MysqlNode node = new MysqlNode(Program.mysqlOption, "SELECT `no` FROM restaurant ORDER BY no");
            MysqlNode update = new MysqlNode(Program.mysqlOption, "UPDATE restaurant SET `computed_waiting` = ?data WHERE no=?no");
            while (true)
            {
                Program.LogSystem.AddLog(2, "AutoWaitingComputing", "갱신을 시작합니다");
                using (node.ExecuteReader())
                {
                    while(node.Read())
                    {
                        update["no"] = node.GetInt("no");
                        int time = Compute(DateTime.Now.AddDays(0), node.GetInt("no"));
                        if (time == -1) update["data"] = null;
                        else update["data"] = time;
                        update.ExecuteNonQuery();
                    }
                }
                // 10분에 한번씩 계산
                Thread.Sleep(60000 * 10);
            }
        }
        /// <summary>
        /// node를 실행하고 time 주변만 저장.
        /// </summary>
        /// <param name="node"></param>
        /// <param name="time"></param>
        /// <returns></returns>
        private static double SQLExecute(MysqlNode node, DateTime set_date)
        {
            TimeSpan set_time = new TimeSpan(set_date.Hour, set_date.Minute, set_date.Second);

            List<int> data = new List<int>();
            using (node.ExecuteReader())
            {
                while (node.Read())
                {
                    DateTime item_date = node.GetDateTime("date");
                    TimeSpan item_time = new TimeSpan(item_date.Hour, item_date.Minute, item_date.Second);

                    TimeSpan span = item_time - set_time;
                    double minute = Math.Abs(span.TotalMinutes);
                    if (minute <= 30)
                        data.Add(node.GetInt("waiting"));
                }
            }
            double avg = 0;
            foreach (int i in data) avg += i;
            if (data.Count == 0) return -1;
            else  return avg / data.Count;
        }
        /// <summary>
        /// 해당 시간에 맞는 음식점의 대기 시간을 반환합니다.
        /// </summary>
        /// <param name="date">기준 시간대로 + 30 - 30분의 데이터를 가져옵니다.</param>
        /// <param name="rest_no"></param>
        public static int Compute(DateTime date, int rest_no)
        {
            // 0 -> 6  일요일 
            // 1 -> 0  월요일
            // 2 -> 1
            int weekend = ((int)(date.DayOfWeek)  + 6) %  7;
            // 같은 요일 데이터 수집
            MysqlNode node = null;
            double temp = -1;
            List<double> list = new List<double>();

            node = new MysqlNode(Program.mysqlOption, "SELECT * FROM waiting_data where restaurant_no = ?no AND date >= SUBDATE(?date, INTERVAL 30 DAY) AND WEEKDAY(`date`) = ?week");
            node["no"] = rest_no;
            node["date"] = date;
            node["week"] = weekend;
            temp = SQLExecute(node, date);
            if (temp != -1) list.Add(temp);

            node = new MysqlNode(Program.mysqlOption, "SELECT * FROM waiting_data where restaurant_no = ?no AND date >= SUBDATE(?date, INTERVAL 8 DAY) AND WEEKDAY(`date`) = ?week");
            node["no"] = rest_no;
            node["date"] = date;
            node["week"] = weekend;
            temp = SQLExecute(node, date);
            if (temp != -1) list.Add(temp);

            node = new MysqlNode(Program.mysqlOption, "SELECT * FROM waiting_data where restaurant_no = ?no AND date(`date`) = date(?date)");
            node["no"] = rest_no;
            node["date"] = date;
            node["week"] = weekend;
            temp = SQLExecute(node, date);
            if (temp != -1) list.Add(temp);


            if (list.Count == 0) return -1;

            if (list.Count == 1) return (int)list[0];
            else
            {
                double time = list[0];
                list.RemoveAt(0);
                foreach(double item in list)
                {
                    time = time * 0.6 + item * 0.4;
                }
                return (int)time;
            }
        }
    }
}
