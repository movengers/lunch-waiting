using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EasyMysql
{
    public class MysqlOption
    {
        public MysqlOption(string host, string database, string id, string password, string option = null)
        {
            Host = host;
            ID = id;
            Password = password;
            Option = option;
            Database = database;
        }

        public string Host { get; protected set; }
        public string ID { get; protected set; }
        public string Password { get; protected set; }
        public string Option { get; protected set; }
        public string Database { get; protected set; }
        public override string ToString()
        {
            return String.Format("Server={0};Database={1};Uid={2};Pwd={3};{4}", Host, Database, ID, Password, Option);
        }
    }
}
