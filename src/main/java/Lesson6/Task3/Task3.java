package Lesson6.Task3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Task3 {
    private static final Logger log = LoggerFactory.getLogger(Task3.class.getName());

    public static void main(String[] args) {
        int[] source = new int[] { 1, 1, 1, 1, 4, 4, 4, 1, 1 };
        log.debug("Исходный массив" + Arrays.toString(source));
        boolean res = task3Checker(source, 1,4);

        log.debug("Результат анализа " + res);
    }

    // Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть одной четверки или
    // единицы, то метод вернет false; Написать набор тестов для этого метода (по 3-4 варианта входных данных).
    // [ 1 1 1 4 4 1 4 4 ] -> true
    // [ 1 1 1 1 1 1 ] -> false
    // [ 4 4 4 4 ] -> false
    // [ 1 4 4 1 1 4 3 ] -> false
    static boolean task3Checker(int[] source, int targetItem1, int targetItem2) {
        return IntStream.of(source).anyMatch(item -> (item == targetItem1)) &&
                IntStream.of(source).anyMatch(item -> (item == targetItem2));
    }
}
