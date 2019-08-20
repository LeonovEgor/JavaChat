package Lesson1.Task2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Main {
    public static void main(String[] args) {
        String[] names = new String[] {"Маша", "Вася", "Катя", "Петя"};
        System.out.println("Массив: " + Arrays.toString(names));
        ArrayList<String> list = toArrayList(names);
        System.out.println("ArrayList: " + list.toString());


        Double[] digits = new Double[] {1.23, 2.34, 3.56};
        System.out.println("Массив: " + Arrays.toString(digits));
        ArrayList<Double> listDouble = toArrayList(digits);
        System.out.println("ArrayList: " + listDouble.toString());
    }

    //Написать метод, который преобразует массив в ArrayList;
    private static <T> ArrayList<T> toArrayList(T[] array) {
        ArrayList<T> list = new ArrayList<>();
        for (T item: array)
            list.add(item);

        return list;
    }
}
