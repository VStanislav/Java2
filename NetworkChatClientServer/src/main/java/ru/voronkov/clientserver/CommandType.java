package ru.voronkov.clientserver;

public enum CommandType {

    AUTH,
    AUTH_OK,
    ERROR,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    UPDATE_USER_LIST,
    UPDATE_USERNAME,
    END
}
