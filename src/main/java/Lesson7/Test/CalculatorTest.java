package Lesson7.Test;

import Lesson7.TestFramework.*;
import Lesson7.WorkingPackage.Calculator;

public class CalculatorTest {

    @BeforeSuite
    public void before() {
        System.out.println("Выполняется метод before");
    }

    @AfterSuite
    public void after() {
        System.out.println("Выполняется метод after");
    }

    @Test(level = 1)
    public void sum1Test() {
        int sum = Calculator.sum(0, 0);
        try {
            Assert.equals(0, sum, "Сумма");
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    @Test(level = 1)
    public void sum2Test() {
        int sum = Calculator.sum(3, 5);
        try {
            Assert.equals(8, sum, "Сумма");
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    @Test(level = 2)
    public void divTest() {
        int div = Calculator.div(10, 2);
        try {
            Assert.equals(5, div, "Деление");
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    @Test(level = 3)
    public void subTest() {
        int sub = Calculator.sub(37, 10);
        try {
            Assert.equals(27, sub, "Вычитание");
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    @Test(level = 4)
    public void multTest() {
        int mult = Calculator.mult(12, 12);
        try {
            Assert.equals(144, mult, "Умножение");
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

}
