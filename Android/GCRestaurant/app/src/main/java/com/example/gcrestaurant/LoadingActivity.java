package com.example.gcrestaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity implements NetworkReceiveInterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        NetworkService.setListener(this);

        // 서비스 시작
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 최종 패킷 리스너에서 해당 액티비티 제거
        NetworkService.removeListener(this);
    }

    protected void loginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    protected void mainActivity(){
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void ReceivePacket(JSONObject json)
    {
        // 로딩 액티비티가 열린 상태에서 로그인 메세지가 전달되면 액티비티를 이동함.
        try
        {
            switch (json.getInt("type"))
            {
                case PacketType.Login:
                    Intent intent = null;
                    if (json.getBoolean("result"))
                    {
                        intent = new Intent(this, MainActivity.class);
                    }
                    else
                    {
                        intent = new Intent(this,LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                    break;
            }
        }
        catch (Exception e)
        {

        }
        NetworkService.SendDebugMessage("액티비티가 패킷을 수신함");
    }
}
