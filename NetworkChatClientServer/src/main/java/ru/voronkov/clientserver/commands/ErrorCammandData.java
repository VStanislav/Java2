package ru.voronkov.clientserver.commands;

import java.io.Serializable;

public class ErrorCammandData implements Serializable {

    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorCammandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
