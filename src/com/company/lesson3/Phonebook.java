package com.company.lesson3;

import java.util.HashMap;
import java.util.Map;

public class Phonebook {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();

        map.put("985","Simens");
        map.put("485","Jons");
        map.put("223","Simens");
        map.put("729","Simens");
        map.put("364","Parker");
        map.put("755","Parker");

        for (Map.Entry<String, String> maps : map.entrySet()) {
            if (maps.getValue().contains("Parker")){
                System.out.println("maps = " + maps.getValue() + " " + maps.getKey());
            }
        }
    }
}
