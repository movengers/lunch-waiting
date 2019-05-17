using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NetworkLibrary;
namespace GCRestaurantServer
{
    public class OnlineUser
    {
        public int id = 0;
        public bool login = false;
        public string name = null;

        public ESocket socket { get; private set; }
        public OnlineUser(ESocket socket)
        {
            this.socket = socket;
        }
    }
}
