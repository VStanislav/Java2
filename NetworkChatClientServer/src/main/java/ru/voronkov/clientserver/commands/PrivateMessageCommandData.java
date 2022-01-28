package ru.voronkov.clientserver.commands;

import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    private final String receiver;
    private final String message;

    public String getMessage() {
        return message;
    }
    public String getReceiver() {
        return receiver;
    }

    public PrivateMessageCommandData(String receiver,String message) {
        this.message = message;
        this.receiver = receiver;

    }


}
