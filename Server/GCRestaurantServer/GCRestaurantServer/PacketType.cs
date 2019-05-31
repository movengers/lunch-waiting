using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GCRestaurantServer
{
    public static class PacketType
    {
        public const int Login = 1; // 클라이언트로부터
        public const int Debug = 1000;

        public const int RestaurantInfo = 1001;

        public const int Message = 1002;
        public const int RestaurantWaitingList = 1003;

        public const int GetRestaurantID = 1004;

        public const int ReadBoard = 3000;
        public const int WriteBoardItem = 3001;
        public const int DeleteBoardItem = 3002;

    }
}
