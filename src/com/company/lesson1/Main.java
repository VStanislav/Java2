package com.company.lesson1;

public class Main {
    public static void main(String[] args) {

        Barriers[] barriers = {
                new Wall(),
                new Treadmill()
        };
        Active[] players = {
                new Cat((int) (Math.random() * 300),(int) (Math.random() * 300)),
                new Human((int) (Math.random() * 300),(int) (Math.random() * 300)),
                new Robot((int) (Math.random() * 300),(int) (Math.random() * 300))
        };

        for (Active pl : players) {

            System.out.println("\nЛимит дистанции и прыжка участника : " + pl.getDistLimit() + " & " +pl.getHeightLimit());

            for (Barriers br : barriers) {
                if (br.isActive(pl)) {
                   System.out.println("Испытание пройдено");
                } else {
                    System.out.println("Провал участника. Выбывает из борьбы.");
                    break;
                }
            }
        }

    }
}
