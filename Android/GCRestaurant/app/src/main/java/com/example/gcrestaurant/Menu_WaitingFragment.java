package com.example.gcrestaurant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class Menu_WaitingFragment extends NetworkFragment implements OnMapReadyCallback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "눌림", Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_menu_waiting,container, false);

        return view;
    }
    JSONArray MapMark = null;
    @Override
    public void ReceivePacket(JSONObject json)
    {
        try
        {
            switch (json.getInt("type"))
            {
                case PacketType.RestaurantWaitingList:
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_f);
                    NetworkService.SendDebugMessage(getChildFragmentManager().getFragments().toString());
                    MapMark = json.getJSONArray("list");
                    mapFragment.getMapAsync(this);
                    break;
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        NetworkService.SendMessage(PacketType.RestaurantWaitingList);
    }
    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng SEOUL = new LatLng(37.448828,127.127128);

        if (MapMark != null) {
            for (int i = 0; i < MapMark.length(); i++) {
                try {
                    JSONObject item = MapMark.getJSONObject(i);
                    LatLng lla = new LatLng(item.getDouble("y"), item.getDouble("x"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(lla);
                    markerOptions.title(item.getString("title"));
                    if (item.isNull("time"))
                        markerOptions.snippet("대기시간 정보 없음");
                    else
                        markerOptions.snippet("대기시간 " + item.getString("time") + "분");

                    map.addMarker(markerOptions);
                } catch (Exception e) {

                }

            }

        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 17));
    }
}
