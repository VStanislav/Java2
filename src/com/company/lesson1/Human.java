package com.company.lesson1;

public class Human implements Active {

    private final int humanDistLimit;
    private final int humanHeightLimit;

    public Human(int humanDistLimit, int humanHeightLimit) {
        this.humanDistLimit = humanDistLimit;
        this.humanHeightLimit = humanHeightLimit;
    }

    @Override
    public int getDistLimit() {
        return humanDistLimit;
    }

    @Override
    public int getHeightLimit() {
        return humanHeightLimit;
    }

    @Override
    public int run() {
        System.out.print("Human run : ");
        return humanDistLimit;
    }

    @Override
    public int jump() {
        System.out.print("Human jump : ");
        return humanHeightLimit;
    }
}

