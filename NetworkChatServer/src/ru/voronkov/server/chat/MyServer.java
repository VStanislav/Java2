package ru.voronkov.server.chat;

import ru.voronkov.server.chat.auth.AuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final List<ClientHandler> clients = new ArrayList<>();
    private AuthService authService;

    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер стартанул");
            authService = new AuthService();
            while (true) {
                waitAndProcessClientConnection(serverSocket);
            }

        }catch (IOException e){
            System.err.println("Не удалось занять переданный порт");
            e.printStackTrace();
        }
    }

    private void waitAndProcessClientConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Ожидание нового подключения");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Клиент подключился");
        ClientHandler clientHandler = new ClientHandler(this,clientSocket);
        this.clients.add(clientHandler);
        clientHandler.handle();

    }

    public void broadCastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public void subscribe (ClientHandler clientHandler){
        this.clients.add(clientHandler);
    }

    public void unsubscribe (ClientHandler clientHandler){
        this.clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
