package com.quang.daapp.stomp;

public enum StompHeader {
    ACCEPT_VERSION("accept-version"),
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    DESTINATION("destination"),
    HOST("host"),
    ID("id"),
    MESSAGE_ID("message-id"),
    RECEIPT_ID("receipt-id"),
    RECEIPT("receipt"),
    SUBSCRIPTION("subscription"),
    VERSION("version");

    private final String value;

    private StompHeader(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
