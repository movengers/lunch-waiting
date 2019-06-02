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
            double lon1 = longitude;
            double lon2 = oher_position.longitude;
            double lat1 = latitude;
            double lat2 = oher_position.latitude;

            double theta, dist;
            theta = lon1 - lon2;
            dist = Math.Sin(deg2rad(lat1)) * Math.Sin(deg2rad(lat2)) + Math.Cos(deg2rad(lat1))
                  * Math.Cos(deg2rad(lat2)) * Math.Cos(deg2rad(theta));
            dist = Math.Acos(dist);
            dist = rad2deg(dist);

            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;    // 단위 mile 에서 km 변환.  
            dist = dist * 1000.0;      // 단위  km 에서 m 로 변환  

            return dist;
        }
        // 주어진 도(degree) 값을 라디언으로 변환  
        private double deg2rad(double deg)
        {
            return (double)(deg * Math.PI / (double)180d);
        }

        // 주어진 라디언(radian) 값을 도(degree) 값으로 변환  
        private double rad2deg(double rad)
        {
            return (double)(rad * (double)180d / Math.PI);
        }
    }
}
