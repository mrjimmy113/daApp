package com.quang.daapp.stomp;

public interface StompMessageListener {

    /**
     * Subscription message received callback.
     *
     * @param stompFrame STOMP message frame
     */
    void onMessage(StompFrame stompFrame);
}
