package com.quang.daapp.stomp;

public class StompSubscription {
    private final Integer id;
    private final String destination;
    private final StompMessageListener listener;

    public StompSubscription(Integer id, String destination, StompMessageListener listener) {
        this.id = id;
        this.destination = destination;
        this.listener = listener;
    }

    public Integer getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public StompMessageListener getListener() {
        return listener;
    }
}
