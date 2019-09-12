package Lesson7.TestFramework;

public class TestException extends Exception {
    private int expected;
    private int value;
    private String message;

    public TestException(int expected, int value, String message) {
        super(message);

        this.expected = expected;
        this.value = value;
        this.message = message;
    }

    @Override
    public void printStackTrace() {
        System.out.println("Expected = " + expected);
        System.out.println("Value = " + value);
        System.out.println("Message" + message);

        super.printStackTrace();
    }
}
