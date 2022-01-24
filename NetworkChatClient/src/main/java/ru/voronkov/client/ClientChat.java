package ru.voronkov.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.voronkov.client.controllers.AuthController;

import java.io.IOException;

public class ClientChat extends Application {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8189;
    public static final String CONNECTION_MESSAGE_ERROR = "Не возможно утсановить сетевое соединение";

    private Stage primaryStage;

    private Stage authStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage =stage;

        ClientController controller = createChatDialog(stage);

        createAuthDialog();

        controller.initializeMessageHandler();
    }

    private void createAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader();
        authLoader.setLocation(getClass().getResource("authDialog.fxml"));
        AnchorPane authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);


        authStage.setScene(new Scene(authDialogPanel));
        AuthController authController = authLoader.getController();
        authController.setClientChat(this);
        authController.initializeMessageHandler();
        authStage.showAndWait();
    }

    private ClientController createChatDialog(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("chat-template.fxml"));

        Parent load = fxmlLoader.load();
        Scene scene = new Scene(load);

        this.primaryStage.setTitle("Chat for GB university");
        this.primaryStage.setScene(scene);

        ClientController controller = fxmlLoader.getController();
        controller.userList.getItems().addAll("userName1",
                "userName2",
                "userName3");

        stage.show();

        connectToServer(controller);
        return controller;
    }

    private void connectToServer(ClientController clientController) {

            boolean result = Network.getInstance().connect();

            if (!result){
                String errorMessage = CONNECTION_MESSAGE_ERROR;
                System.err.println(errorMessage);
                showErrorDialog(errorMessage);
                return;
            }

            clientController.setApplication(this);

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Network.getInstance().close();
                }
            });
    }

    public void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();

    }

    public Stage getAuthStage() {
        return authStage;
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public Stage getChatStage() {
        return this.primaryStage;
    }
}