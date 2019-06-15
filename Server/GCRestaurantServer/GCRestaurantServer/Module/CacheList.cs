using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GCRestaurantServer
{
    public class CacheList<class_type>
    {
        private int due_time = 5;
        private List<Item> items = new List<Item>();
        private Object lock_object = new object();
        public class Item
        {
            public class_type item;
            public DateTime end_time;
            public Item(class_type data, int seconds)
            {
                item = data;
                UpdateTime(seconds);
            }
            public void UpdateTime(int seconds)
            {
                end_time = DateTime.Now.AddSeconds(seconds);
            }
        }
        public CacheList()
        {

        }
        public CacheList(int due)
        {
            due_time = due;
        }
        public bool Add(class_type data, int Seconds = -1)
        {
            if (Seconds == -1) Seconds = due_time;
            // 먼저 데이터가 있는지 검사
            lock (lock_object)
            {
                foreach (Item item in items)
                {
                    if (item.item.Equals(data))
                    {
                        item.UpdateTime(Seconds);
                        return false;
                    }
                }
                items.Add(new Item(data, Seconds));
            }
            return true;
        }
        public bool Contains(class_type data)
        {
            CleanArray();
            lock (lock_object)
            {
                foreach (Item item in items)
                {
                    if (item.item.Equals(data))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        public List<class_type> Values
        {
            get
            {
                CleanArray();
                List<class_type> result = new List<class_type>();
                lock (lock_object)
                {
                    for (int i = 0; i < items.Count; i++)
                    {
                        result.Add(items[i].item);
                    }
                }
                return result;
            }
        }
        public void CleanArray()
        {
            lock (lock_object)
            {
                List<Item> Result = new List<Item>();
                foreach (Item item in items.ToArray())
                {
                    if (item.end_time > DateTime.Now)
                    {
                        Result.Add(item);
                    }
                }
                items = Result;
            }
        }
        public override string ToString()
        {
            string A = "";
            lock (lock_object)
            {
                for (int i = 0; i < items.Count; i++)
                {
                    if (i != 0) A += ", ";
                    A += items[i].item + ": " + items[i].end_time;
                }
            }
            return "{" + A + "}";
        }
    }
}
