package ru.voronkov.server.chat;

import ru.voronkov.clientserver.Command;
import ru.voronkov.clientserver.CommandType;
import ru.voronkov.clientserver.commands.AuthCommandData;
import ru.voronkov.clientserver.commands.PrivateMessageCommandData;
import ru.voronkov.clientserver.commands.PublicMessageCommandData;

import java.io.*;
import java.net.Socket;

public class ClientHandler {


    private final MyServer server;
    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String userName;

    public ClientHandler(MyServer myServer,Socket clientSocket) {
        this.server = myServer;
        this.clientSocket = clientSocket;
    }

    public void  handle() throws IOException {
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
            try {
                authenticate();
                readMessages();
            } catch (IOException e) {
                System.err.println("Не смогли прочитать сообщения клиента");
                e.printStackTrace();
            }finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Не смогли закрыть соединение");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void authenticate() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command==null){
                continue;
            }

            if (command.getType() == CommandType.AUTH){
                AuthCommandData date = (AuthCommandData) command.getData();

                String login = date.getLogin();
                String password = date.getPassword();

                String userName = server.getAuthService().getUserNameByLoginAndPassword(login, password);
//                System.out.println(userName);

                if (userName == null){
                    sendCommand(Command.errorCommand("Неверные логин и пароль"));
                }else if (server.isUserNameBusy(userName)){
                    sendCommand(Command.errorCommand("Пользователь занят/ Busy"));
                }else {
//                    System.out.println("есть контакт?");
                    this.userName=userName;
                    sendCommand(Command.authOkCommand(userName));
                    server.subscribe(this);
                    return;
                }
//                System.out.println("после проверок");
            }
        }
    }

    public void sendCommand(Command command) throws IOException {
        outputStream.writeObject(command);
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Не смогли прочитать класс Command ");
            e.printStackTrace();
        }
        return command;
    }

    private void readMessages() throws IOException {
        while (true){
            Command command = readCommand();

            if (command==null){
                continue;
            }

            switch (command.getType()){
                case END:
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData date = (PrivateMessageCommandData) command.getData();
                    String recipient = date.getReceiver();
                    String privateMessage = date.getMessage();
                    server.sendPrivateMessage(this, recipient, privateMessage);
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData date = (PublicMessageCommandData) command.getData();
                    processMessage(date.getMessage());
                }
            }
        }
    }

    private void processMessage(String message) throws IOException {
        this.server.broadCastMessage(message,this);
    }

    private void closeConnection () throws IOException {
        server.unsubscribe(this);
        clientSocket.close();
    }

    public String getUserName() {
        return userName;
    }
}
