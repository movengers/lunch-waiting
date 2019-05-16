package com.example.gcrestaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class LoadingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // 서비스 시작
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
    }
    @Override
    public void onResume()
    {
        super.onResume();
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
}
