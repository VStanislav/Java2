package com.company.lesson1;

public class Treadmill implements Barriers {

    private final int distance = 150;

    public Treadmill() {
    }

    @Override
    public boolean isActive(Active anyone){
        System.out.println("Бег.дорожка : " + distance);
        return anyone.run() >= distance;
    }
}
