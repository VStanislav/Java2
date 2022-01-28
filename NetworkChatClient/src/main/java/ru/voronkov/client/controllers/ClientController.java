package ru.voronkov.client.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.voronkov.client.ClientChat;
import ru.voronkov.client.model.Network;
import ru.voronkov.client.model.ReadCommandListner;
import ru.voronkov.clientserver.Command;
import ru.voronkov.clientserver.CommandType;
import ru.voronkov.clientserver.commands.ClientMessageCommandData;
import ru.voronkov.clientserver.commands.UpdateUserListCommandData;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ClientController {

    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    public ListView<String> userList;

    private ClientChat application;

    public void sendMessage() {
        String message = textField.getText().trim();

        if (message.isEmpty()) {
            textField.clear();
            return;
        }

        String sender = null;
        if (!userList.getSelectionModel().isEmpty()) {
            sender = userList.getSelectionModel().getSelectedItem();
        }

        try {
            if (sender != null){
                Network.getInstance().sendPrivateMessage(sender,message);
            }else {
                Network.getInstance().sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            application.showErrorDialog("Ошибка передачи данных по сети");
        }

        appendMessageToChat("Я", message);
    }

    private void appendMessageToChat(String sender, String message) {
        textArea.appendText(DateFormat.getDateInstance().format(new Date()));
        textArea.appendText(System.lineSeparator());

        if (sender != null) {
            textArea.appendText(sender + ": ");
            textArea.appendText(System.lineSeparator());
        }

        textArea.appendText(message);
        textArea.appendText(System.lineSeparator());
        textArea.appendText(System.lineSeparator());
        textField.setFocusTraversable(true);
        textField.clear();
    }

    public void initializeMessageHandler() {
        Network.getInstance().addReadMessageListner(new ReadCommandListner() {
            @Override
            public void processReceivedCommand(Command command) {
                if (command.getType() == CommandType.CLIENT_MESSAGE) {
                    ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                    appendMessageToChat(data.getSender(), data.getMessage());
                } else if (command.getType() == CommandType.UPDATE_USER_LIST) {
                    UpdateUserListCommandData data = (UpdateUserListCommandData) command.getData();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            userList.setItems(FXCollections.observableList(data.getUsers()));
                        }
                    });
                }
            }
        });
    }
}
