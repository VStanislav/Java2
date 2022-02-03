package ru.voronkov.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import ru.voronkov.client.ClientChat;
import ru.voronkov.client.dialogs.Dialogs;
import ru.voronkov.client.model.Network;
import ru.voronkov.client.model.ReadCommandListner;
import ru.voronkov.clientserver.Command;
import ru.voronkov.clientserver.CommandType;
import ru.voronkov.clientserver.commands.AuthOkCommandData;
import ru.voronkov.clientserver.commands.ErrorCammandData;

import java.io.IOException;

public class AuthController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button authButton;

    private ReadCommandListner readMessageListner;

    @FXML
    public void executeAuth(ActionEvent actionEvent) {

        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }

        if (!connectToServer()) {
            Dialogs.NetworkError.SERVER_CONNECT.show();
        }

        try {
            Network.getInstance().sendAuthMessage(login,password);
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
            e.printStackTrace();
        }
    }

    private boolean connectToServer() {
        Network network = Network.getInstance();
        return network.isConnected() || network.connect();
    }

    public void initializeMessageHandler() {
        readMessageListner = getNetwork().addReadMessageListner(new ReadCommandListner() {
            @Override
            public void processReceivedCommand(Command command) {
                if (command.getType()== CommandType.AUTH_OK) {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    String userName = data.getUserName();
                    Platform.runLater(() -> {
                        ClientChat.INSTANCE.switchToMainChatWindow(userName);
                    });
                } else if (command.getType() == CommandType.ERROR){
                    ErrorCammandData data = (ErrorCammandData) command.getData();
                    String errorMessage = data.getErrorMessage();
                    Platform.runLater(() -> {
                        Dialogs.AuthError.INVALID_CREDENTIALS.show(errorMessage);
                    });
                }
            }
        });
    }

    public void close(){
        getNetwork().removeReadMessageListner(readMessageListner);
    }

    private Network getNetwork() {
        return Network.getInstance();
    }
}
