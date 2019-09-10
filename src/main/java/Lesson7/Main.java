package Lesson7;

import Lesson7.WorkingPackage.Calculator;

public class Main {
    public static void main(String[] args) {
        int sum = Calculator.sum(5, 10);
        System.out.println(sum);

        int div = Calculator.div(10, 2);
        System.out.println(div);

        int mult = Calculator.mult(4, 8);
        System.out.println(mult);

        int sub = Calculator.sub(37, 10);
        System.out.println(sub);

    }
}
