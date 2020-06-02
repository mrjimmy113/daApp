package com.quang.daapp.test;

import java.util.HashMap;
import java.util.Map;

public class StompFrame {
    private StompCommand command;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    /**
     * Create frame from string.
     *
     * @param data Data string
     * @return STOMP frame instance
     */
    public static StompFrame fromString(String data) {
        String[] lines = data.split("\n");
        int pos = 0;

        // Ignore empty lines
        while (pos < lines.length && lines[pos].isEmpty()) {
            pos++;
        }

        // Command
        StompCommand command = StompCommand.fromValue(lines[pos++]);

        // Headers
        Map<String, String> headers = new HashMap<>();
        while (!lines[pos].isEmpty()) {
            String[] keyValue = lines[pos].split(":");
            headers.put(keyValue[0], keyValue[1]);
            pos++;
        }
        pos++;

        // Body
        StringBuilder body = new StringBuilder();
        for (; pos < lines.length; pos++) {
            body.append(lines[pos]);
            if (body.charAt(body.length() - 1) == '\0') {
                body.deleteCharAt(body.length() - 1);
                break;
            }
        }

        return new StompFrame(command, headers, body.toString());
    }

    public StompFrame(StompCommand command) {
        this(command, null, null);
    }

    public StompFrame(StompCommand command, Map<String, String> headers) {
        this(command, headers, null);
    }

    public StompFrame(StompCommand command, Map<String, String> headers, String body) {
        this.command = command;
        this.body = body;

        if (headers != null) {
            for (String k : headers.keySet()) {
                this.headers.put(k, headers.get(k));
            }
        }
    }

    public StompCommand getCommand() {
        return command;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Command
        sb.append(command);
        sb.append('\n');
        // Headers
        for (String k : headers.keySet()) {
            sb.append(k);
            sb.append(':');
            sb.append(headers.get(k));
            sb.append('\n');
        }
        sb.append('\n');
        // Body
        if (body != null) {
            sb.append(body);
        }
        sb.append('\0');

        return sb.toString();
    }
}
