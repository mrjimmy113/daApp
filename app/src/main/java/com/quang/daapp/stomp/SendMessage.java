package com.quang.daapp.stomp;

public class SendMessage {

    private boolean isExpert;
    private String message;
    private MessageType type;

    public SendMessage(boolean isExpert, String message, MessageType type) {
        this.isExpert = isExpert;
        this.message = message;
        this.type = type;
    }

    public boolean isExpert() {
        return isExpert;
    }
    public void setExpert(boolean isExpert) {
        this.isExpert = isExpert;
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
