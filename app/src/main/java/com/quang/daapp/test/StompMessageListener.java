package com.quang.daapp.test;

public interface StompMessageListener {

    /**
     * Subscription message received callback.
     *
     * @param stompFrame STOMP message frame
     */
    void onMessage(StompFrame stompFrame);
}
