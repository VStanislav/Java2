package com.company.Lesson6.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServerMy {
    private static final int PORT = 8189;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static Socket socket;

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server START !!!");
            socket = serverSocket.accept();
            System.out.println("User CONNECT !!!");
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String messageFromServer = inputStream.readUTF();
                            if (messageFromServer.equals("/end")) {
                                System.out.println("Цикл по чтению у сервера закончен");
                                break;
                            }
                            System.out.println("От клиента: " + messageFromServer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Thread threadWrite = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            outputStream.writeUTF(reader.readLine());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadWrite.setDaemon(true);
            threadWrite.start();


        } catch (IOException e) {
            System.out.println("Error. Wrong connection to port " + PORT);
            e.printStackTrace();
        }
    }

    public static void closeConnection(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Все порты закрыты");
        }catch (IOException e){
            System.out.println("Ошибка закрытия потоков");
            e.printStackTrace();
        }
    }
}
