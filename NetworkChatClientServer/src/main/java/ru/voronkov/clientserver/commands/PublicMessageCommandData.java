package ru.voronkov.clientserver.commands;

import java.io.Serializable;

public class PublicMessageCommandData implements Serializable {

    private final String message;

    public String getMessage() {
        return message;
    }

    public PublicMessageCommandData(String message) {
        this.message = message;
    }
}
