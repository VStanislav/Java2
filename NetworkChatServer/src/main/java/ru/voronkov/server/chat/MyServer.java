package ru.voronkov.server.chat;

import ru.voronkov.clientserver.Command;
import ru.voronkov.server.chat.auth.IAuthService;
import ru.voronkov.server.chat.auth.PersistentDbAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {

    private final List<ClientHandler> clients = new ArrayList<>();
    private IAuthService authService;
    private ExecutorService executorService;

    public IAuthService getAuthService() {
        return authService;
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер стартанул");
            authService = createAuthService();
            authService.start();
            executorService = Executors.newCachedThreadPool();
            while (true) {
                waitAndProcessClientConnection(serverSocket);
            }

        } catch (IOException e) {
            System.err.println("Не удалось занять переданный порт");
            e.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

        private IAuthService createAuthService () {
            return new PersistentDbAuthService();
        }

        private void waitAndProcessClientConnection (ServerSocket serverSocket) throws IOException {
            System.out.println("Ожидание нового подключения");
            Socket clientSocket = serverSocket.accept();

            System.out.println("Соединение прошло");
            ClientHandler clientHandler = new ClientHandler(this, clientSocket);
            clientHandler.handle();

        }

        public synchronized void broadCastMessage (String message, ClientHandler sender) throws IOException {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendCommand(Command.clientMessageCommand(sender.getUserName(), message));
                }
            }
        }

        public synchronized void sendPrivateMessage (ClientHandler sender, String recipient, String privateMessage) throws
        IOException {
            for (ClientHandler client : clients) {
                if (client != sender && client.getUserName().equals(recipient)) {
                    client.sendCommand(Command.clientMessageCommand(sender.getUserName(), privateMessage));
                    break;
                }
            }
        }

        public synchronized boolean isUserNameBusy (String userName){
            for (ClientHandler client : clients) {
                System.out.println(client);
                if (client.getUserName().equals(userName)) {
                    return true;
                }
            }
            return false;
        }

        public synchronized void subscribe (ClientHandler clientHandler) throws IOException {
            this.clients.add(clientHandler);
            notifyClientUserListUpdated();
        }

        public synchronized void unsubscribe (ClientHandler clientHandler) throws IOException {
            this.clients.remove(clientHandler);
            notifyClientUserListUpdated();
        }

        public void notifyClientUserListUpdated () throws IOException {
            List<String> userListOnline = new ArrayList<>();

            for (ClientHandler client : clients) {
                userListOnline.add(client.getUserName());
            }

            for (ClientHandler client : clients) {
                client.sendCommand(Command.updateUserListCommand(userListOnline));
            }
        }
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
