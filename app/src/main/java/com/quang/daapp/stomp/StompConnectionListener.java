package com.quang.daapp.stomp;

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
