package com.quang.daapp.ultis;

import rx.Observer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class WebSocketClient {
    private static StompClient stompClient;
    public static final String SOCKET_URL = "ws://192.168.42.251:8080";
    public static StompClient getStompClient() {
        if(stompClient == null) {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL+ "/chat");
            stompClient.connect();
        }

        return stompClient;
    }

    public static void subcribeChat(String channel) {
        if(stompClient != null) {
            stompClient.topic("/topic/messages." + channel).subscribe(new Observer<StompMessage>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(StompMessage stompMessage) {

                }
            });

        }
    }

    public static void sendChat(String channel, String message) {
        if(stompClient != null) {
            stompClient.send("/app/chat." + channel).subscribe();
        }
    }







    public static void disconnectClient() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
    }
}
