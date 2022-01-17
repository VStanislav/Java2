package com.company.Lesson6.ClientFX;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat extends Application {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8189;
    public static final String CONNECTION_MESSAGE_ERROR = "Не возможно утсановить сетевое соединение";

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage=stage;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("chat-template.fxml"));

        Parent load = fxmlLoader.load();
        Scene scene = new Scene(load);

        this.stage.setTitle("Chat for GB university");
        this.stage.setScene(scene);

        ClientController controller = fxmlLoader.getController();
        controller.userList.getItems().addAll("User1","User2");

        stage.show();

        connectToServer(controller);
    }

    private void connectToServer(ClientController clientController) {

            Network network = new Network();
            boolean result = network.connect();

            if (!result){
                String errorMessage = CONNECTION_MESSAGE_ERROR;
                System.err.println(errorMessage);
                showErrorDialog(errorMessage);
                return;
            }
            clientController.setNetwork(network);
            clientController.setApplication(this);

            this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    network.close();
                }
            });
    }

    public void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();

    }

    public static void main(String[] args) {
        Application.launch();
    }
}