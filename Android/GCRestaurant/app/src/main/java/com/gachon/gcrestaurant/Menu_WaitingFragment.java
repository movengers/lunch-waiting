package com.gachon.gcrestaurant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menu_WaitingFragment extends NetworkFragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "눌림", Toast.LENGTH_LONG).show();
        View view = inflater.inflate(R.layout.fragment_menu_waiting, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_f);
        mapFragment.getMapAsync(this);

        return view;
    }

    JSONArray MapMark = null;

    @Override
    public void ReceivePacket(JSONObject json) {
        try {
            switch (json.getInt("type")) {
                case PacketType.RestaurantWaitingList:
                    MapMark = json.getJSONArray("list");
                    mapFragment.getMapAsync(this);
                    break;
                case PacketType.GetRestaurantID:
                    SwitchView(Menu_RestaurantDetail.newInstance(json.getInt("no")));
                    break;
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        NetworkService.SendMessage(PacketType.RestaurantWaitingList);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.448828, 127.127128), 17));

        List<String> location_move = new ArrayList<>();
        if (MapMark != null) {
            for (int i = 0; i < MapMark.length(); i++) {
                try {
                    JSONObject item = MapMark.getJSONObject(i);
                    double y = item.getDouble("y");
                    double x = item.getDouble("x");
                    // 만약 겹치는 음식점을 겹치지 않게 보여주고 싶다면 아래 주석 해제
                    /*
                    String temp =  String.valueOf(y) + String.valueOf(x);
                    while(location_move.contains(temp))
                    {
                        x += 0.00002;
                        y -= 0.00002;
                        temp =  String.valueOf(y) + String.valueOf(x);
                    }
                    location_move.add(temp);
                    */
                    LatLng lla = new LatLng(y, x);
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
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                NetworkService.SendMessage(PacketType.GetRestaurantID, "title", marker.getTitle());

            }
        });
    }
}


