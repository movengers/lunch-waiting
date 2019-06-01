package com.gachon.gcrestaurant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.kakao.auth.Session;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;


public class ESocket extends Thread {
    public static ESocket instance = null;


    private Socket socket = null;
    private BufferedReader inFromClient = null;
    private PrintWriter outToClient = null;
    private Handler sendHandler= null;

    private List<JSONObject> sendmessage_waiting = new LinkedList<>();

    public ESocket()
    {
        instance = this;

        GlobalApplication ga = GlobalApplication.getGlobalApplicationContext();
        ga.requestAccessTokenInfo();

    }
    public void run() {
        // 완전히 종료 명령이 오기 전까지는 계속 반복한다.
        while(instance == this)
        {
            try {
                SocketAddress socketAddress = new InetSocketAddress("easyrobot.co.kr", 1231);

                socket = new Socket();
                socket.setSoTimeout(10000);
                socket.connect(socketAddress,1000);
                inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToClient = new PrintWriter(socket.getOutputStream(), true);

                // 카카오톡 로그인 토큰 가져오기
                String token = Session.getCurrentSession().getTokenInfo().getAccessToken();

                JSONObject LoginMessage = new JSONObject();
                try
                {
                    LoginMessage.put("type",PacketType.Login);
                    LoginMessage.put("token",token);
                    NetworkService.SendMessage(LoginMessage);
                } catch (Exception e) {
                    NetworkService.SendDebugMessage(e.toString());

                }

                new Thread()
                {
                    public void run()
                    {
                        // 정상적으로 연결 되었다면 기존 Send 버퍼를 모두 읽는다.
                        while(sendmessage_waiting.size() > 0)
                        {
                            JSONObject object = sendmessage_waiting.get(0);
                            sendmessage_waiting.remove(0);
                            outToClient.write(object + "\r\n");
                            outToClient.flush();
                        }
                        Looper.prepare();
                        sendHandler = new Handler() {
                            public void handleMessage(Message msg) {

                                String message = msg.getData().getString("data");

                                Log.d("소켓", "처리" + message);
                                outToClient.write(message + "\r\n");
                                outToClient.flush();
                            }
                        };
                        Looper.loop();
                    }
                }.start();

                String data;
                while ((data = inFromClient.readLine()) != null && instance == this)
                {
                    Handler handler = NetworkService.instance.ReceiveHandler;
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
            catch (Exception e)
            {
                Log.d("소켓 통신", e.toString());
            }
            finally {
                SocketClose();
            }
        }
    }
    private void SocketClose()
    {
        if (sendHandler != null) {
            sendHandler.getLooper().quit();
            sendHandler = null;
        }
        if (outToClient != null) {
            outToClient.close();
            outToClient = null;
        }
        try {
            if (inFromClient != null) {
                inFromClient.close();
                inFromClient = null;
            }
        } catch (IOException e) { }
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) { }
    }
    public void SendMessage(JSONObject json)
    {
        if (sendHandler != null) {
            Message message = sendHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("data", json + "");
            message.setData(bundle);
            sendHandler.sendMessage(message);
            Log.d("소켓", "입력");
        }
        else
        {
            sendmessage_waiting.add(json);
        }
    }
}
