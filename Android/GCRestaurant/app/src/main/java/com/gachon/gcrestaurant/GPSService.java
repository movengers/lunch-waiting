package com.gachon.gcrestaurant;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

public class GPSService implements LocationListener {
    public static GPSService instance = null;
    private Context c;
    public GPSService(Context cx)
    {
        instance = this;
        this.c = cx;
        NetworkService.SendDebugMessage("GPS 서비스 인스턴스 생성");
    }
    public void Start()
    {
        if (c != null)
        {
            NetworkService.SendDebugMessage("GPS 서비스 실행");
            long minTime = 10000;
            float minDistance = 0;
            LocationManager manager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);

            try
            {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime, minDistance, this);
                NetworkService.SendDebugMessage("GPS 서비스 리스너 등록");
            }
            catch (SecurityException e)
            {
                Log.d("에러", e.toString());
            }
        }
    }
    public void onLocationChanged(Location location)
    {
        NetworkService.SendDebugMessage("GPS 서비스 리스너 실행됨");

        // 만약 지금 실행되는 인스턴스가 마지막 인스턴스가 아닌경우 (새로 다른게 만들어졌으면)
        if (instance != this)
        {
            LocationManager manager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
            // 지금 인스턴스를 제거
            manager.removeUpdates(this);
        }
        Double latitude = location.getLatitude();  Double longitude = location.getLongitude();
        try
        {
            JSONObject json = new JSONObject();
            json.put("type", PacketType.PositionUpdate);
            json.put("latitude", latitude);
            json.put("longitude", longitude);
            NetworkService.SendMessage(json);
        }
        catch (Exception e)
        {

        }
    }
    public void onProviderDisabled(String provider)
    {

    }
    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}
