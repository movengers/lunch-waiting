package com.example.gcrestaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

public class LoginActivity extends Activity implements NetworkReceiveInterface  {
    private SessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        NetworkService.setListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkService.removeListener(this);
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback{
        @Override
        public void onSessionOpened() {
            NetworkService.Connect();
            //redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null){
                Logger.e(exception);
            }
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
                    Intent intent = null;
                    if (json.getBoolean("result")) {
                        intent = new Intent(this, MainActivity.class);
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
        catch (Exception e)
        {

        }
        NetworkService.SendDebugMessage("액티비티가 패킷을 수신함");
    }
}
