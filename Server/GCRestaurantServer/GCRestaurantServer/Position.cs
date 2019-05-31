using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
namespace GCRestaurantServer
{
    public class Position
    {
        public double latitude { get; private set; }
        public double longitude { get; private set; }
        public Position(double latitude, double longitude)
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public Position(JObject json)
        {
            this.latitude = (double)json["latitude"];
            this.longitude = (double)json["longitude"];
        }
        public double DistanceToMeter(Position oher_position)
        {
            // 수정 필요
            return 0;
        }
    }
}
