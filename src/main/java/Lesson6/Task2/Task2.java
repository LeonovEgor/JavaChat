package Lesson6.Task2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Task2 {
    private static final Logger log = LoggerFactory.getLogger(Task2.class.getName());

    public static void main(String[] args) {
        int[] source = new int[] { 1, 2, 4, 4, 2, 3, 4, 1, 7 };
        log.debug("Исходный массив" + Arrays.toString(source));
        int[] dest;
        try {
            dest = task2Converter(source, 4);
        } catch (RuntimeException e) {
            log.error("Неверные входные данные", e);
            dest = new int[] {};
        }

        log.debug("Результат поиска" + Arrays.toString(dest));
    }

    // 2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
    // Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
    // идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку, иначе в методе
    // необходимо выбросить RuntimeException. Написать набор тестов для этого метода (по 3-4 варианта входных данных).
    // Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
    static int[] task2Converter(int[] sourceArray, int targetItem) {
        int[] destArray;
        int index = -1;
        for (int i = sourceArray.length - 1; i >= 0; i--) {
            if (sourceArray[i] == targetItem)  {
                index = i;
                break;
            };
        }

        if (index == -1) throw new RuntimeException("В переданном массиве отсутствует искомый элемент");

        // Если элемент последний
        if (sourceArray.length - 1 != index) {
            destArray = new int[sourceArray.length - index - 1];
            System.arraycopy(sourceArray, index+1, destArray, 0, sourceArray.length - index - 1);
        }
        else {
            destArray = new int[] {};
        }

        return destArray;
    }
}
