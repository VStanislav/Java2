package com.company;

public class Main {
    private static int SIZE = 4;
    private static final String[][] array1 = new String[][]
            {{"2", "5", "6", "4"},
                    {"2", "5", "6", "4"},
                    {"2", "5", "6", "4"},
                    {"2", "5", "6", "4"}};
    private static final String[][] array2 = new String[][]
            {{"2", "5", "6", "4"},
                    {"2", "5", "f", "4"},
                    {"2", "5", "6", "4"},
                    {"2", "5", "6", "4"}};
    public static final String[][] array3 = new String[][]
            {{"2", "5", "6", "4"},
                    {"2", "5", "6", "4", "6"},
                    {"2", "5", "6",},
                    {"2", "5", "6", "4"}};
    public static void main(String[] args) {
        try {
            arrayTest(array3);
        }catch (MyArraySizeException e){
            System.out.println(e.getMessage());
        }

    }
    public static void arrayTest (String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr.length != SIZE || arr[i].length != SIZE) {
                    System.out.println("сработало");
                    throw new MyArraySizeException();
                }
                System.out.println("Круг 2го Фор:" + (j+1));
            }
            System.out.println("Круг 1го фор -  "+(i+1) );
        }
        System.out.println("OFFFFFF");
    }
}

class MyArrayDataException extends RuntimeException {

    public MyArrayDataException(String cellValue, int row, int col) {
        super(String.format("Неверное значение '%s' в ячейки [%d][%d]", cellValue, row, col));
    }
}

class MyArraySizeException extends RuntimeException {

    public MyArraySizeException() {
        super("Не вырный  индекс массива!");
    }
}
