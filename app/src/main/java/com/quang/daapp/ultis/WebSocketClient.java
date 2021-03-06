package com.quang.daapp.ultis;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.quang.daapp.BuildConfig;
import com.quang.daapp.R;
import com.quang.daapp.stomp.MessageType;
import com.quang.daapp.stomp.ReceiveMessage;
import com.quang.daapp.stomp.SendMessage;
import com.quang.daapp.stomp.StompClient;
import com.quang.daapp.stomp.StompConnectionListener;
import com.quang.daapp.stomp.StompFrame;
import com.quang.daapp.stomp.StompMessageListener;
import com.quang.daapp.stomp.StompSubscription;
import com.quang.daapp.ui.dialog.MessageDialogFragment;
import com.quang.daapp.ui.other.UnAuthActivity;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WebSocketClient {
    private static WebSocketClient instance;

    public StompClient stompClient;

    private Map<Integer, MutableLiveData<ReceiveMessage>> subscribes = new HashMap<>();
    private List<StompSubscription> stompSubscriptions = new ArrayList<>();



    public static WebSocketClient getInstance() {

        if(instance == null) {
            instance = new WebSocketClient();

        }
        return instance;
    }
    public void clear() {
        subscribes.clear();
        for (StompSubscription s:
             stompSubscriptions) {
            stompClient.removeSubscription(s);
        }
        stompSubscriptions.clear();
    }

    public void connect(Context context) {

        stompClient = new StompClient(URI.create("ws://" + BuildConfig.API_URL + "chat?token=" + AuthTokenManager.getToken(context)));

        boolean connected = true;
        try {
            connected = instance.stompClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        if (!connected) {
            MessageDialogFragment dialogTimeOut = new MessageDialogFragment(
                    "Can not connect to server, please check your connection"
                    , R.color.colorDanger, R.drawable.ic_error, () -> {
                Intent intent = new Intent(context, UnAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                connect(context);
            }
            );
            DialogManager.getInstance().showDialog(dialogTimeOut,true);


        }
    }

    public void subscribe(int channel) {
        if(stompClient ==null) return;
        if(!stompClient.isStompConnected()) {
            stompClient.connect();
        }
        MutableLiveData<ReceiveMessage> mutableLiveData = new MutableLiveData<>();

        if(!subscribes.containsKey(channel)) {
            stompSubscriptions.add( stompClient.subscribe("/topic/messages."  + channel, new StompMessageListener() {

                @Override
                public void onMessage(StompFrame stompFrame) {

                    mutableLiveData.postValue(NetworkClient.getInstance().getGson().fromJson(stompFrame.getBody(),ReceiveMessage.class));

                }


            }));
            subscribes.putIfAbsent(channel,mutableLiveData);
        }


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
