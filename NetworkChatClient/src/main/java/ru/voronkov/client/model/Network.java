package ru.voronkov.client.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.voronkov.clientserver.Command;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {

    private static final Logger LOGGER = LogManager.getLogger(Network.class);

    private final List<ReadCommandListner> listners = new CopyOnWriteArrayList<>();

    private static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8189;

    private final int port;
    private final String host;
    private Socket socket;
    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;

    private static Network INSTANCE;
    private boolean connected;
    private String currentUsername;
    private final ExecutorService executorService;


    public static Network getInstance(){
        if (INSTANCE==null){
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    private Network(int port, String host) {
        this.host = host;
        this.port = port;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    private Network() {
        this(SERVER_PORT,SERVER_HOST);
    }

    public boolean connect(){
        try {
            this.socket = new Socket(this.host,this.port);
            this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
            this.socketInput = new ObjectInputStream(socket.getInputStream());
            startReadMessageProcess();
            connected=true;
            return true;
        }catch (IOException e){
            e.printStackTrace();
            LOGGER.error("Не удалось установить соединение");
            return false;
        }
    }

    public void sendAuthMessage(String login, String password) throws IOException {
        sendCommand(Command.authCommand(login,password));
    }

    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessageCommand(message));
    }

    private void sendCommand(Command command) throws IOException {
        try {
            socketOutput.writeObject(command);
        }catch (IOException e){
            LOGGER.error("Не удалось отправить сообщение на сервер");
            throw e;
        }
    }

    public void sendPrivateMessage(String recipient,String message) throws IOException {
        sendCommand(Command.privateMessageCommand(recipient,message));
    }

    public void startReadMessageProcess(){
        executorService.execute(() -> {
            while (true) {
                try {
                    if (Thread.currentThread().isInterrupted()){
                        return;
                    }

                    Command command = readCommand();

                    if (command.getType() == null) {
                        continue;
                    }

                    for (ReadCommandListner messageListner : listners) {
                        messageListner.processReceivedCommand(command);
                    }

                } catch (IOException e) {
                    LOGGER.error("Не удалось прочитать сообщение  от сервера");
                    e.printStackTrace();
                    close();
                    break;
                }
            }
        });
    }

    private Command readCommand() throws IOException {
        Command command = null;

        try {
            command = (Command) socketInput.readObject();
        } catch (ClassNotFoundException e) {
            LOGGER.error("Не смогли прикастить класс");
            e.printStackTrace();
        }

        return command;

    }

    public ReadCommandListner addReadMessageListner(ReadCommandListner listner){
        listners.add(listner);
        return listner;
    }

    public void removeReadMessageListner (ReadCommandListner listner){
        listners.remove(listner);
    }

    public void close(){
        try {
            connected=false;
            executorService.shutdownNow();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void changeUsername(String newUsername) throws IOException {
        sendCommand(Command.updateUsernameCommand(newUsername));
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

}
