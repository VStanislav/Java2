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
import ru.voronkov.client.controllers.ClientController;
import ru.voronkov.client.model.Network;
import ru.voronkov.client.service.ClientHistory;

import java.io.IOException;

public class ClientChat extends Application {

    public static ClientChat INSTANCE;

    private FXMLLoader chatWindowLoader;
    private FXMLLoader authLoader;
    private Stage primaryStage;
    private Stage authStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage =stage;

        initViews();
        getChatStage().show();
        getAuthStage().show();
        getAuthController().initializeMessageHandler();
    }

    @Override
    public void init() throws Exception {
        INSTANCE = this;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Network.getInstance().close();
        ClientController.getClientHistoryService().close();
    }

    public void initViews() throws IOException {

        initChatWindow();
        initAuthDialog();
    }

    private void initAuthDialog() throws IOException {
        authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource("authDialog.fxml"));

        Parent authDialogPanel = authLoader.load();

        authStage = new Stage();
        authStage.initOwner(primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
    }

    private void initChatWindow() throws IOException {
        chatWindowLoader = new FXMLLoader();
        chatWindowLoader.setLocation(getClass().getResource("chat-template.fxml"));

        Parent root = chatWindowLoader.load();
        this.primaryStage.setScene(new Scene(root));
    }

    public void switchToMainChatWindow(String userName) {
        getChatStage().setTitle(userName);
        getChatController().initializeMessageHandler();
        getAuthController().close();
        getAuthStage().close();
    }

    public void showErrorDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();

    }

    public AuthController getAuthController(){
        return authLoader.getController();
    }

    public ClientController getChatController(){
        return chatWindowLoader.getController();
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