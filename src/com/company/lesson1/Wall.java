package com.company.lesson1;

public class Wall implements Barriers {

    private final int wallHeight = 150;


    public Wall() {
    }

    @Override
    public boolean isActive(Active anyone) {
        System.out.println("Стена : " + wallHeight);
        return anyone.jump() >= wallHeight;
    }

}
