package com.example.gcrestaurant;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Menu_HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {



        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"comment")
                .setSmallIcon(R.drawable.kakaostory_icon)
                .setContentTitle("보낸사람")
                .setContentText("제목");
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder)
                .bigText("제목" + "\r\n\r\n" + "내용");


        builder.setStyle(style);

        NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        return inflater.inflate(R.layout.fragment_menu_home,container, false);
    }
}
