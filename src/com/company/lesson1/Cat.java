package com.company.lesson1;

public class Cat implements Active {

    private final int catDistLimit;
    private final int catHeightLimit;


    public Cat(int catDistLimit, int catHeightLimit) {
        this.catDistLimit = catDistLimit;
        this.catHeightLimit = catHeightLimit;
    }

    @Override
    public int getDistLimit() {
        return catDistLimit;
    }

    @Override
    public int getHeightLimit() {
        return catHeightLimit;
    }

    @Override
    public int run() {
        System.out.print("Cat run : ");
        return catDistLimit;
    }

    @Override
    public int jump() {
        System.out.print("Cat jump : ");
        return catHeightLimit;
    }


}
