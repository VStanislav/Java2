package com.company.Lesson6.Client;

import java.io.IOException;

public class Chat {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.openConnection();
            System.out.println("Подключение к серверу успешно");

        } catch (IOException e) {
            System.out.println("Ошибка подключения в классе Chat");
            e.printStackTrace();
        }


    }
}
