package com.quang.daapp.ultis;

import android.util.Log;

import com.quang.daapp.test.StompClient;
import com.quang.daapp.test.StompFrame;
import com.quang.daapp.test.StompMessageListener;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WebSocketClient {
    public static WebSocketClient instance;

    public StompClient stompClient;
    private Map<String, MutableLiveData<String>> subscribes = new HashMap<>();

    public static WebSocketClient getInstance() {
        if(instance == null) {
            instance = new WebSocketClient();
            instance.connect();
            boolean connected = true;
            try {
                connected = instance.stompClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            if (!connected) {
                Log.e("CLMN","Failed to connect to the socket");

            }
        }
        return instance;
    }

    public void connect() {
        stompClient = new StompClient(URI.create("ws://192.168.137.1:8080/chat"));
    }

    public void subscribe(String channel) {
        if(stompClient ==null) return;
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        subscribes.put(channel,mutableLiveData);
        stompClient.subscribe("/topic/messages."  + channel, new StompMessageListener() {

            @Override
            public void onMessage(StompFrame stompFrame) {
                mutableLiveData.setValue(stompFrame.getBody());
            }


        });
    }

    public void chat(String channel, String message) {
        if(stompClient ==null) return;
        stompClient.send("/app/chat." + channel, message);
    }

    public LiveData<String> getSubscribeChannelData(String channel) {
        return  subscribes.get(channel);
    }

}
