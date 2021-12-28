package com.company.lesson3;

import java.util.*;

public class Duplicates {
    public static void main(String[] args) {
        int count;

        List<String> arrList = new ArrayList<>();
        arrList.add("Мост");
        arrList.add("Апельсин");
        arrList.add("Аист");
        arrList.add("Мост");
        arrList.add("Вектор");
        arrList.add("Мост");
        arrList.add("Net");
        arrList.add("NET");
        arrList.add("We");
        arrList.add("Horizon");
        arrList.add("Мост");
        arrList.add("Мост");
        arrList.add("Horizon");

        Set<String> setList = new HashSet<>(arrList);

        for (String s : setList) {
            count=0;
            for (String l : arrList) {
                if (s.equals(l)){
                    count++;
                }
            }
            System.out.println("Слово : "+s+" встречается " + count+" раз.");

        }


    }


}
