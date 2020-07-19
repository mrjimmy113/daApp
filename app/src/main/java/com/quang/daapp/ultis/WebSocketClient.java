package com.quang.daapp.ultis;

import android.content.Context;
import android.util.Log;

import com.quang.daapp.BuildConfig;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.stomp.StompClient;
import com.quang.daapp.stomp.StompConnectionListener;
import com.quang.daapp.stomp.StompFrame;
import com.quang.daapp.stomp.StompMessageListener;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WebSocketClient {
    private static WebSocketClient instance;

    public StompClient stompClient;

    private Map<Integer, MutableLiveData<ReceiveMessage>> subscribes = new HashMap<>();

    public static WebSocketClient getInstance() {

        if(instance == null) {
            instance = new WebSocketClient();

        }
        return instance;
    }

    public void connect(Context context) {
        subscribes.clear();
        stompClient = new StompClient(URI.create("ws://" + BuildConfig.API_URL + "chat?token=" + AuthTokenManager.getToken(context)));

        boolean connected = true;
        try {
            connected = instance.stompClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        if (!connected) {
            Log.e("CLMN","Failed to connect to the socket");
            connect(context);

        }
    }

    public void subscribe(int channel) {
        if(stompClient ==null) return;
        if(!stompClient.isStompConnected()) {
            stompClient.connect();
        }
        MutableLiveData<ReceiveMessage> mutableLiveData = new MutableLiveData<>();
        subscribes.putIfAbsent(channel,mutableLiveData);
        stompClient.subscribe("/topic/messages."  + channel, new StompMessageListener() {

            @Override
            public void onMessage(StompFrame stompFrame) {

                mutableLiveData.postValue(NetworkClient.getInstance().getGson().fromJson(stompFrame.getBody(),ReceiveMessage.class));

            }


        });
    }

    public void chat(int channel, SendMessage message) {
        if(stompClient ==null) return;
        if(!stompClient.isStompConnected()) return;
        stompClient.send("/app/chat." + channel, NetworkClient.getInstance().getGson().toJson(message));
    }

    public LiveData<ReceiveMessage> getSubscribeChannelData(int channel) {
        return  subscribes.get(channel);
    }

    public Map<Integer, MutableLiveData<ReceiveMessage>> getSubscribes() {
        return subscribes;
    }


}
