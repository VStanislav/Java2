package ru.voronkov.client.model;

import ru.voronkov.clientserver.Command;

public interface ReadCommandListner {
    void processReceivedCommand(Command command);
}
