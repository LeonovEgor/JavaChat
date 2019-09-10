package Lesson6.Task2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Task2Test {
    private static final Logger log = LoggerFactory.getLogger(Task2.class.getName());

    private int[] source;
    private int[] destination;

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]
        {
                {new int[] {4, 1, 2, 3, 4}, new int[] {}}, // Последний элемент
                {new int[] {4, 1, 2, 3}, new int[] {1, 2, 3}},  // Первый элемент
                {new int[] {4, 1, 4, 3, 2}, new int[] {3, 2}},  // Средний элемент
                {new int[] {-4, 4, -4, -4}, new int[] {-4, -4}},  // С отрицательными числами
        });
    }

    public Task2Test(int[] source, int[] destination) {
        this.source = source;
        this.destination = destination;
    }

    @Before
    public void init() {
        //
    }

    // Проверка основных данных
    @Test
    public void task2Converter() {
        //Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
        int[] dest = Task2.task2Converter(source, 4);
        log.debug("Исходный  " + Arrays.toString(source));
        log.debug("Результат " + Arrays.toString(dest));
        log.debug("Ожидался  " + Arrays.toString(destination));
        log.debug("---------------------------------------------");
        Assert.assertArrayEquals( destination, dest );
    }

    // Проверка исключения
    @Test(expected = RuntimeException.class)
    public void task2ConverterException() {
        int[] sourceWoTargetItem = new int[] { 1, 2, 0, 0, 2, 3, 0, 1, 7 };
        int[] dest = Task2.task2Converter(sourceWoTargetItem, 4);
    }

}