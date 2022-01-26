package ru.voronkov.clientserver.commands;

import java.io.Serializable;

public class ClientMessageCommandData implements Serializable {

    private final String sender;
    private final String message;

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public ClientMessageCommandData(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}
