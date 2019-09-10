package Lesson1.Task1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String[] names = new String[] {"Маша", "Вася", "Катя", "Петя"};
        System.out.println("Массив до изменения: " + Arrays.toString(names));
        try {
            change(names, 1, 3);
        }
        catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        System.out.println("Массив после изменения: " + Arrays.toString(names));


        Integer[] numbers = new Integer[] {1, 2, 3, 4, 5, 6, 7};
        System.out.println("Массив до изменения: " + Arrays.toString(numbers));
        try {
            change(numbers, 1, 5);
        }
        catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        System.out.println("Массив после изменения: " + Arrays.toString(numbers));


    }

    //1. Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
    private static <T> void change(T[] array, int firstIndex, int secondIndex) throws IndexOutOfBoundsException {
        if (firstIndex > array.length -1)
            throw new IndexOutOfBoundsException("Первый индекс за пределами массива");
        if (secondIndex > array.length -1)
            throw new IndexOutOfBoundsException("Второй индекс за пределами массива");

        T temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }
}
