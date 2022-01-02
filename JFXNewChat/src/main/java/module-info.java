module com.example.jfxnewchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jfxnewchat to javafx.fxml;
    exports com.example.jfxnewchat;
}