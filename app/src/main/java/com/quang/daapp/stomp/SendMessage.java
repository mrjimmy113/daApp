package com.quang.daapp.stomp;

public class SendMessage {
    private String message;
    private MessageType type;

    public SendMessage(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }




}
