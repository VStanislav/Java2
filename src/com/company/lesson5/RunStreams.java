package com.company.lesson5;

public class RunStreams {

    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {

        float[] arr = new float[size];

        for (int i = 0; i < size ; i++) {
            arr[i] = 1;
        }

        firstMethod(arr);
        secondMethod(arr);
    }

    public static synchronized void firstMethod(float[] arr){

        long a = System.currentTimeMillis();

        for (int i = 0; i < size ; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.println(System.currentTimeMillis() - a);
    }

    public static synchronized void secondMethod(float[] arr){

        float[] arr2 = new float[h],arr3 = new float[h];

        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, arr2, 0, h);
        System.arraycopy(arr, h, arr3, 0, h);

        Thread thread = new Thread(() -> {

            for (int i = 0; i < h ; i++) {
                arr2[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }

        });
        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < h ; i++) {
                arr3[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }

        });

        thread.start();
        thread2.start();

        System.arraycopy(arr2, 0, arr, 0, h);
        System.arraycopy(arr3, 0, arr, h, h);

        System.out.println(System.currentTimeMillis() - a);

    }
}
