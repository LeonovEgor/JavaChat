package Lesson7.WorkingPackage;

public class  Calculator {
    public static int sum(int arg1, int arg2) {
        return arg1 + arg2;
    }

    public static int div(int arg1, int arg2) {
        if (arg2 == 0) throw new IllegalArgumentException("Деление на 0 запрещено!");
        return arg1/arg2;
    }

    public static int mult(int arg1, int arg2) {
        return arg1 * arg2;
    }

    public static int sub(int arg1, int arg2) {
        return arg1 - arg2;
    }
}
