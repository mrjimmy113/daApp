package com.quang.daapp.test;

public abstract class StompConnectionListener {

    /**
     * STOMP is in establishing connection state.
     */
    public void onConnecting() {
    }

    /**
     * STOMP has been successfully connected.
     */
    public void onConnected() {
    }

    /**
     * STOMP has been disconnected.
     */
    public void onDisconnected() {
    }
}
