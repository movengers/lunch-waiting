package com.gachon.gcrestaurant;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

public class Menu_HomeFragment extends Fragment {
    public RemoteViews contentView;
    public Intent intent1,intent2;
    public PendingIntent pintent1,pintent2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_menu_home,container, false);
    }
}
