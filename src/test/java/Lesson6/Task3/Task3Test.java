package Lesson6.Task3;

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
public class Task3Test {
    private static final Logger log = LoggerFactory.getLogger(Task3.class.getName());

    private int[] source;
    private boolean result;

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList(new Object[][]
                {
                        {new int[] {4, 1, 2, 3, 4}, true}, // Содержит оба искомых значения
                        {new int[] {4, 2, 2, 3}, false},  // Содержит только 4
                        {new int[] {1, 1, 1, 1, 1}, false},  // Содержит одни единицы
                        {new int[] {4, 4, 4, 4}, false},  // Содержит одни четверки
                });
    }

    public Task3Test(int[] source, boolean result) {
        this.source = source;
        this.result = result;
    }

    @Before
    public void init() {
        //
    }

    // Проверка основных данных
    @Test
    public void task2Converter() {
        boolean res = Task3.task3Checker(source, 1, 4);
        log.debug("Исходный  " + Arrays.toString(source));
        log.debug("Результат " + res);
        log.debug("Ожидался  " + result);
        log.debug("---------------------------------------------");
        Assert.assertEquals(result, res);
    }
}