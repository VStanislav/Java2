package com.company.lesson1;

public class Robot implements Active {

    private final int robotDistLimit;
    private final int robotHeightLimit;

    public Robot(int robotDistLimit, int robotHeightLimit) {
        this.robotDistLimit = robotDistLimit;
        this.robotHeightLimit = robotHeightLimit;
    }

    @Override
    public int getDistLimit() {
        return robotDistLimit;
    }

    @Override
    public int getHeightLimit() {
        return robotHeightLimit;
    }

    @Override
    public int run() {
        System.out.print("Robot run : ");
        return robotDistLimit;
    }

    @Override
    public int jump() {
        System.out.print("Robot jump : ");
        return robotHeightLimit;
    }
}
