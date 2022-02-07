package ru.voronkov.client.model;

import ru.voronkov.clientserver.Command;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Network {

    private final List<ReadCommandListner> listners = new CopyOnWriteArrayList<>();

    private static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8189;

    private final int port;
    private final String host;
    private Socket socket;
    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;

    private static Network INSTANCE;
    private Thread readMessageProcess;
    private boolean connected;
    private String currentUsername;


    public static Network getInstance(){
        if (INSTANCE==null){
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    private Network(int port, String host) {
        this.host = host;
        this.port = port;
    }

    private Network() {
        this(SERVER_PORT,SERVER_HOST);
    }
    public boolean connect(){
        try {
            this.socket = new Socket(this.host,this.port);
            this.socketOutput = new ObjectOutputStream(socket.getOutputStream());
            this.socketInput = new ObjectInputStream(socket.getInputStream());
            readMessageProcess = startReadMessageProcess();
            connected=true;
            return true;
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("Не удалось установить соединение");
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
            System.err.println("Не удалось отправить сообщение на сервер");
            throw e;
        }
    }

    public void sendPrivateMessage(String recipient,String message) throws IOException {
        sendCommand(Command.privateMessageCommand(recipient,message));
    }

    public Thread startReadMessageProcess(){
        Thread thread = new Thread(() -> {
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
                    System.err.println("Не удалось прочитать сообщение  от сервера");
//                    e.printStackTrace();
                    close();
                    break;
                }
            }
        });
        thread.setDaemon(true) ;
        thread.start();
        return thread;
    }

    private Command readCommand() throws IOException {
        Command command = null;

        try {
            command = (Command) socketInput.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Не смогли прикастить класс");
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
            readMessageProcess.interrupt();
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
