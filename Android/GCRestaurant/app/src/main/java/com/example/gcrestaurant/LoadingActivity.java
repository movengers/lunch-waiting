package com.example.gcrestaurant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity implements NetworkReceiveInterface {

    int GPS_permission = PackageManager.PERMISSION_DENIED;
    int Login = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        NetworkService.setListener(this);

        // add moving gif
        ImageView sushi = (ImageView) findViewById(R.id.sushi_gif);
        ImageView hotdog = (ImageView) findViewById(R.id.hotdog_gif);
        ImageView pizza = (ImageView) findViewById(R.id.pizza_gif);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(sushi);
        Glide.with(this).load(R.drawable.sushi_resize).into(gifImage);
        GlideDrawableImageViewTarget gifImage2 = new GlideDrawableImageViewTarget(hotdog);
        Glide.with(this).load(R.drawable.hotdog_resize).into(gifImage2);
        GlideDrawableImageViewTarget gifImage3 = new GlideDrawableImageViewTarget(pizza);
        Glide.with(this).load(R.drawable.pizza_resize4).into(gifImage3);

        if (NetworkService.instance != null) NetworkService.Connect();
        // 서비스 시작
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);

        GPS_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (GPS_permission == PackageManager.PERMISSION_GRANTED) {
            View view = findViewById(R.id.GPS_BOX);
            view.setVisibility(View.INVISIBLE);
        } else {
            Button GPS_Button = findViewById(R.id.GPS_PERMISSION);
            final LoadingActivity activity_instance = this;
            GPS_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GPS_permission != PackageManager.PERMISSION_GRANTED) {


                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity_instance,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                            // 일반적인 취소
                        } else {
                            // 처음 또는 무조건 무시
                        }
                        ActivityCompat.requestPermissions(activity_instance,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                1000);
                    }

                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPS_permission = grantResults[0];
                    StartNextActivity();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 최종 패킷 리스너에서 해당 액티비티 제거
        NetworkService.removeListener(this);
    }

    private void StartNextActivity()
    {
        if (Login != 0 && GPS_permission == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = null;
            if (Login == 1)
                intent = new Intent(this, MainActivity.class);
            else
                intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void ReceivePacket(JSONObject json)
    {
        // 로딩 액티비티가 열린 상태에서 로그인 메세지가 전달되면 액티비티를 이동함.
        try
        {
            switch (json.getInt("type"))
            {
                case PacketType.Login:
                    if (json.getBoolean("result"))
                        Login = 1;
                    else
                        Login = -1;
                StartNextActivity();
                break;
            }
        }
        catch (Exception e)
        {

        }
        NetworkService.SendDebugMessage("액티비티가 패킷을 수신함");
    }
}
