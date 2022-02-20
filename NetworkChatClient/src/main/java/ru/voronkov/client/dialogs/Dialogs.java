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
            showDialog(Alert.AlertType.ERROR,TITLE, TYPE,message);

        }

        public void show(String errorMessage){
            showDialog(Alert.AlertType.ERROR,TITLE, TYPE,errorMessage);

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
            showDialog(Alert.AlertType.ERROR,TITLE, TYPE,message);
        }
    }

    private static void showDialog(Alert.AlertType dialogType, String title, String type, String message){
        Alert alert = new Alert(dialogType);
        alert.initOwner(ClientChat.INSTANCE.getChatStage());
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public enum AboutDialog {
        INFO(String.format("Чат создан %s %n при участии %s %n","Воронков Станислав", "Титов Владимир"));

        private final String message;
        private static final String TITLE = "Информация о программе";
        private static final String TYPE = " 'Проект Online Chat' ver. 1.03";

        AboutDialog(String message) {
            this.message = message;
        }

        public void show() {
            showDialog(Alert.AlertType.INFORMATION, TITLE, TYPE, message);
        }
    }
}
