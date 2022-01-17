package com.company.Lesson6.ClientFX;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.function.Consumer;

public class ClientController {

  @FXML
  private TextArea textArea;
  @FXML
  private TextField textField;
  @FXML
  private Button sendButton;
  @FXML
  public ListView<String> userList;

  private Network network;
  private ClientChat application;

  public void sendMessage() {
    String message = textField.getText();
    appendMessageToChat(message);

    try {
      network.sendMessage(message);
    } catch (IOException e) {
      e.printStackTrace();
      application.showErrorDialog("Ошибка передачи данных по сети");
    }
  }

  private void appendMessageToChat(String message) {
    if (!message.isEmpty()) {
      textArea.appendText(message);
      textArea.appendText(System.lineSeparator());
      textField.clear();
    }
  }

  public void setNetwork(Network network) {
    this.network = network;
    network.waitMessages(new Consumer<String>() {
      @Override
      public void accept(String message) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            appendMessageToChat(message);
          }
        });
      }
    });
  }

  public void setApplication(ClientChat application) {
    this.application = application;
  }
}