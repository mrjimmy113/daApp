package com.quang.daapp.test;


import org.java_websocket.WebSocketListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StompClient extends WebSocketClient {
    /**
     * Current STOMP implementation version.
     */
    private static final String STOMP_VERSION = "1.2";

    /**
     * STOMP topic subscription listeners.
     */
    private Map<Integer, StompSubscription> subscriptions = new HashMap<>();

    /**
     * STOMP connection status.
     */
    private boolean stompConnected;

    /**
     * STOMP connection listener.
     */
    private StompConnectionListener stompConnectionListener;

    /**
     * {@inheritDoc}
     */
    public StompClient(URI serverUri) {
        super(serverUri);
    }

    /**
     * {@inheritDoc}
     */
    public StompClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    /**
     * {@inheritDoc}
     */
    public StompClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        connectStomp();
    }

    @Override
    public void onMessage(String message) {
        StompFrame stompFrame = StompFrame.fromString(message);

        switch (stompFrame.getCommand()) {
            case CONNECTED:
                stompConnected = true;
                if (stompConnectionListener != null) {
                    stompConnectionListener.onConnected();
                }
                break;
            case MESSAGE:
                Integer subscriptionId = Integer.valueOf(
                        stompFrame.getHeaders().get(StompHeader.SUBSCRIPTION.toString())
                );
                subscriptions.get(subscriptionId).getListener().onMessage(stompFrame);
                break;
            case DISCONNECT:
                stompConnected = false;
                if (stompConnectionListener != null) {
                    stompConnectionListener.onDisconnected();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        stompConnected = false;
        if (stompConnectionListener != null) {
            stompConnectionListener.onDisconnected();
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Create STOMP connection.
     */
    protected void connectStomp() {
        if (stompConnected) {
            return;
        }
        if (stompConnectionListener != null) {
            stompConnectionListener.onConnecting();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(StompHeader.ACCEPT_VERSION.toString(), STOMP_VERSION);
        headers.put(StompHeader.HOST.toString(), uri.getHost());

        send(new StompFrame(StompCommand.CONNECT, headers).toString());
    }

    /**
     * Disconnect STOMP.
     */
    protected void disconnectStomp() {
        if (!stompConnected) {
            return;
        }

        send(new StompFrame(StompCommand.DISCONNECT).toString());
    }

    /**
     * Send text message to the server.
     *
     * @param destination destination
     * @param message     text message
     */
    public void send(String destination, String message) {
        send(destination, message, null);
    }

    /**
     * Send text message to the server.
     *
     * @param destination destination
     * @param message     text message
     * @param headers     (optional) additional headers
     */
    public void send(String destination, String message, Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(StompHeader.DESTINATION.toString(), destination);
        StompFrame frame = new StompFrame(StompCommand.SEND, headers, message);

        send(frame.toString());
    }

    /**
     * Subscribe to a specific topic.
     *
     * @param destination topic destination
     * @param listener    listener
     * @return STOMP subscription data that can be used to unsubscribe
     */
    public StompSubscription subscribe(String destination, StompMessageListener listener) {
        StompSubscription subscription = new StompSubscription(UUID.randomUUID().hashCode(), destination, listener);

        Map<String, String> headers = new HashMap<>();
        headers.put(StompHeader.ID.toString(), String.valueOf(subscription.getId()));
        headers.put(StompHeader.DESTINATION.toString(), subscription.getDestination());

        StompFrame frame = new StompFrame(StompCommand.SUBSCRIBE, headers);
        send(frame.toString());

        subscriptions.put(subscription.getId(), subscription);

        return subscription;
    }

    /**
     * Remove a single subscription.
     *
     * @param subscription subscription
     */
    public void removeSubscription(StompSubscription subscription) {
        Map<String, String> headers = new HashMap<>();
        headers.put(StompHeader.ID.toString(), String.valueOf(subscription.getId()));

        StompFrame frame = new StompFrame(StompCommand.UNSUBSCRIBE, headers);
        send(frame.toString());

        subscriptions.remove(subscription.getId());
    }

    /**
     * Unsubscribe all from a single topic.
     *
     * @param destination topic
     */
    public void removeAllSubscriptions(String destination) {
        for (StompSubscription s : subscriptions.values()) {
            if (s.getDestination().equals(destination)) {
                removeSubscription(s);
            }
        }
    }

    /**
     * Check if STOMP is currently connected.
     *
     * @return STOMP connection status
     */
    public boolean isStompConnected() {
        return stompConnected;
    }

    /**
     * Register STOMP conection listener.
     *
     * @param stompConnectionListener listener
     */
    public void setStompConnectionListener(StompConnectionListener stompConnectionListener) {
        this.stompConnectionListener = stompConnectionListener;
    }
}
