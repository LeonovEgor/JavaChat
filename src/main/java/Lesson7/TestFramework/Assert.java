package Lesson7.TestFramework;

public class Assert {
    public static void equals(int expected, int value, String comment) throws TestException {
        if (expected == value)
            System.out.println(String.format("Test Passed. Expected = %d Value = %d. %s", expected, value, comment));
        else
            throw new TestException(expected, value, "Результат не совпадает");
    }
}
