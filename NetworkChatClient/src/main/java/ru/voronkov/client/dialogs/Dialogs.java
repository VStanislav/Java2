package ru.voronkov.client.dialogs;

import javafx.scene.control.Alert;
import ru.voronkov.client.ClientChat;

public class Dialogs {

    public enum AuthError {
        EMPTY_CREDENTIALS ("Логин и пароль должны быть указаны"),
        INVALID_CREDENTIALS ("Логин и пароль заданы не корректно");

        public static final String TITLE = "Ошибка аутентификации";
        public static final String TYPE = TITLE;
        public final String message;

        AuthError(String message) {
            this.message = message;
        }

        public void show(){
            showDialog(Alert.AlertType.ERROR, TYPE,message);

        }

        public void show(String errorMessage){
            showDialog(Alert.AlertType.ERROR, TYPE,errorMessage);

        }
    }

    public enum NetworkError {
        SEND_MESSAGE ("Не удалось отправить сообщение"),
        SERVER_CONNECT ("Не удалось установить соедниение с сервером.");

        public static final String TITLE = "Сетевая ошибка";
        public static final String TYPE = "Ошибка передачи данных по  сети";
        public final String message;

        NetworkError(String message) {
            this.message = message;
        }

        public void show(){
            showDialog(Alert.AlertType.ERROR, TYPE,message);
        }
    }

    private static void showDialog(Alert.AlertType dialogType, String title, String message){
        Alert alert = new Alert(dialogType);
        alert.initOwner(ClientChat.INSTANCE.getChatStage());
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
