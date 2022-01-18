package com.company.Lesson6.Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void openConnection () throws IOException {


        socket = new Socket(HOST,PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                String messageFromServer = inputStream.readUTF();
                                if (messageFromServer.equals("/end")) {
                                    System.out.println("Цикл по чтению у клиента закончен");
                                    break;
                                }
                                System.out.println("От сервера: " + messageFromServer);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Thread threadRead = new Thread(new Runnable() {
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
                threadRead.setDaemon(true);
                threadRead.start();


//                    while (true){
//                        outputStream.writeUTF(reader.readLine());
//                        String messageFromServer = inputStream.readUTF();
//                        if (messageFromServer.equals("/end")){
//                            closeConnection();
//                        }
//                        System.out.println("От сервера: "+messageFromServer);
//
//                    }

//                }catch (IOException e){
//                    System.out.println("Ошибка получения сообщения");
//                    e.printStackTrace();
//                }

            }
        }).start();
    }

    public void closeConnection(){
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
