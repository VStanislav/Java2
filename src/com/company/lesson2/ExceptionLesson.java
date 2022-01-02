package com.company.lesson2;

public class ExceptionLesson {
     public static void main(String[] args) {
        String[][] stringArray = {{"1","2","3"},{"4","5","6"},{"7","8","9"}};

        try {
            System.out.println("Результат суммы всех элементов массива: "+checkLengthAndSum(stringArray));
        }
        catch (MyArraySizeException | MyArrayDataException e){
            System.out.println(e.getMessage());
        }
    }

    public static int checkLengthAndSum(String[][] arr) throws MyArraySizeException,MyArrayDataException{

        int sumArray=0;
//
        if (arr.length==3){
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].length==3){

                }
                else {
                    throw new MyArraySizeException();
                }
            }
            sumArray = checkNumber(arr, sumArray);
        }else throw new MyArraySizeException();

        return sumArray;
    }


    private static int checkNumber(String[][] arr, int getSumArray){

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (isNumber(arr[i][j])) {
                    getSumArray += Integer.parseInt(arr[i][j]);
                }else {
                    System.out.println("Ошибка в ячейке [" + i + "][" + j+"]. Элемент: "+arr[i][j]);
                    System.out.println("Текущий результат суммирования до ошибки :" + getSumArray);
                    throw new MyArrayDataException();
                }
            }
        }
        return getSumArray;
    }

    private static boolean isNumber(String string){

        // на счет этого метода я сомневаюсь до сих пор, т.к. тут так же идет отлавливание ошибок, но
        // как провести проверку "является ли строка числом" я лучше не придумал.

        int value;
        try  {
            value=Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

}

class MyArraySizeException extends RuntimeException{
    public MyArraySizeException(){
        super("Run MyArraySizeException ! Check this!");
    }
}

class MyArrayDataException extends NumberFormatException{
    public MyArrayDataException() {
        super("Rum MyArrayDataException! There must be number.");
    }
}