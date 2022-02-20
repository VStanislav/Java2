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
import ru.voronkov.client.service.ClientHistory;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import ru.voronkov.client.dialogs.Dialogs;
import java.util.Optional;


public class ClientController {

    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    @FXML
    public ListView<String> userList;

    private ClientChat application;
    private static final int LAST_MESSAGES = 100;
    private static ClientHistory clientHistoryService;

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

    public void createChatHistory() {
        clientHistoryService = new ClientHistory(Network.getInstance().getCurrentUsername());
        clientHistoryService.init();
    }

    private void loadChatHistory() {
        String rows = clientHistoryService.loadLastRows2(LAST_MESSAGES);
        textArea.clear();
        textArea.setText(rows);
    }

    private void appendMessageToChat(String sender, String message) {
        String currentText = textArea.getText();
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

        String newMessage = textArea.getText(currentText.length(), textArea.getLength());
        clientHistoryService.appendText(newMessage);
    }

    public void initializeMessageHandler() {
        Network.getInstance().addReadMessageListner(new ReadCommandListner() {
            @Override
            public void processReceivedCommand(Command command) {
                if (clientHistoryService == null) {
                    createChatHistory();
                    loadChatHistory();
                }
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

    public void closeChat(ActionEvent actionEvent) throws Exception {
        clientHistoryService.close();
        Network.getInstance().close();
        ClientChat.INSTANCE.getChatStage().close();
    }

    public void changeUserName(ActionEvent actionEvent) {
        TextInputDialog editDialog = new TextInputDialog();
        editDialog.setTitle("Изменить имя пользователя");
        editDialog.setHeaderText("Введите новое имя пользователя");
        editDialog.setContentText("Имя пользователя:");

        Optional<String> result = editDialog.showAndWait();
        if (result.isPresent()) {
            try {
                Network.getInstance().changeUsername(result.get());
            } catch (IOException e) {
                e.printStackTrace();
                Dialogs.NetworkError.SEND_MESSAGE.show();
            }

        }
    }

    public void about(ActionEvent actionEvent) {
        Dialogs.AboutDialog.INFO.show();
    }

    public void clearButton(ActionEvent actionEvent) {
        textField.clear();
    }

    public static ClientHistory getClientHistoryService() {
        return clientHistoryService;
    }
}
