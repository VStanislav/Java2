package ru.voronkov.server.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.voronkov.clientserver.Command;
import ru.voronkov.clientserver.CommandType;
import ru.voronkov.clientserver.commands.AuthCommandData;
import ru.voronkov.clientserver.commands.PrivateMessageCommandData;
import ru.voronkov.clientserver.commands.PublicMessageCommandData;
import ru.voronkov.clientserver.commands.UpdateUsernameCommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {

    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private final MyServer myServer;
    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private String userName;

    public ClientHandler(MyServer myServer,Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void  handle() throws IOException {
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        myServer.getExecutorService().execute(() -> {

            try {
                    authenticate();
                    readMessages();
                } catch (IOException e) {
                    LOGGER.error("Не смогли прочитать сообщения клиента");
                    e.printStackTrace();
                }finally {
                    try {
                        closeConnection();
                        LOGGER.info("close connection");
                    } catch (IOException e) {
                        LOGGER.error("Не смогли закрыть соединение");
                        e.printStackTrace();
                    }
                }
        });

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

                String userName = myServer.getAuthService().getUserNameByLoginAndPassword(login, password);

                if (userName == null){
                    sendCommand(Command.errorCommand("Неверные логин и пароль"));
                }else if (myServer.isUserNameBusy(userName)){
                    sendCommand(Command.errorCommand("Пользователь занят/ Busy"));
                }else {
                    this.userName=userName;
                    sendCommand(Command.authOkCommand(userName));
                    myServer.subscribe(this);
                    return;
                }
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
            LOGGER.error("Не смогли прочитать класс Command ");
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
                    myServer.sendPrivateMessage(this, recipient, privateMessage);
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData date = (PublicMessageCommandData) command.getData();
                    processMessage(date.getMessage());
                    break;
                }
                case UPDATE_USERNAME: {
                    UpdateUsernameCommandData data = (UpdateUsernameCommandData) command.getData();
                    String newUsername = data.getUsername();
                    myServer.getAuthService().updateUsername(userName, newUsername);
                    userName = newUsername;
                    myServer.notifyClientUserListUpdated();
                    break;
                }
            }
        }
    }

    private void processMessage(String message) throws IOException {
        this.myServer.broadCastMessage(message,this);
    }

    private void closeConnection () throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public String getUserName() {
        return userName;
    }
}
