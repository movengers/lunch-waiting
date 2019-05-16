package com.example.gcrestaurant;

import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;


public class ESocket extends Thread {
    private Socket socket = null;
    private BufferedReader inFromClient = null;
    private PrintWriter outToClient = null;
    private Handler sendHandler= null;

    public ESocket()
    {
        GlobalApplication.socket = this;
    }
    public void run() {
        // 완전히 종료 명령이 오기 전까지는 계속 반복한다.
        while(GlobalApplication.socket == this)
        {
            try {
                SocketAddress socketAddress = new InetSocketAddress("easyrobot.co.kr", 1231);

                socket = new Socket();
                socket.setSoTimeout(10000);
                socket.connect(socketAddress,1000);
                inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToClient = new PrintWriter(socket.getOutputStream(), true);

                new Thread()
                {
                    public void run()
                    {
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
                while ((data = inFromClient.readLine()) != null && GlobalApplication.socket == this)
                {
                    Handler handler = GlobalApplication.getGlobalApplicationContext().ReceiveHandler;
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
    }
}
