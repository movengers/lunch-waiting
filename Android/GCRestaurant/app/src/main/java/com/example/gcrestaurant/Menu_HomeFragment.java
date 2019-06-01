package com.example.gcrestaurant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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

        Intent intent = new Intent(getActivity(), MainActivity.class); //버튼을 누르면 이동할 Activity 또는 Service
        PendingIntent pi = PendingIntent.getActivity(getContext(),(int) System.currentTimeMillis(), intent, 0);

        Bitmap notiIconLarge = BitmapFactory.decodeResource(getResources(),R.drawable.doughnut);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"comment")
                .setSmallIcon(R.drawable.doughnut)
                .setContentTitle("A 식당 대기시간 정보요청이 왔습니다!")
                .setLargeIcon(notiIconLarge)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setColor(Color.rgb(150,150,220))
                .setContentText("")
                .setContentIntent(pi);
        //NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder)
          //      .bigText( "\r\n\r\n" + "기다리는 사람이 있나요?");

        //builder.setStyle(style);
        builder.addAction(R.drawable.ic_menu_send,"없음",pi);
        builder.addAction(R.drawable.ic_menu_camera,"조금",pi);
        builder.addAction(R.drawable.doughnut,"많음",pi);



        NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());


        return inflater.inflate(R.layout.fragment_menu_home,container, false);
    }
}
