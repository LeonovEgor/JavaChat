package Lesson4.Task1;

import java.util.Random;

public class Variant2 {

    enum Letter {
        A,
        B,
        C;
        private static Letter next(Letter currentLetter) {
            if (currentLetter == Letter.A) return Letter.B;
            if (currentLetter == Letter.B) return Letter.C;
            else return Letter.A;
        }
    };

    private static final int COUNT = 5;
    private final Object monitor = new Object();
    private volatile Letter currentLetter = Letter.A;

    public static void main(String[] args) {
        Variant2 w = new Variant2();
        new Thread(() -> {w.print(Letter.A);  }).start();
        new Thread(() -> {w.print(Letter.B);  }).start();
        new Thread(() -> {w.print(Letter.C);  }).start();
    }

    private void print(Letter letter) {
        synchronized (monitor) {
            try {
                for (int i = 0; i < COUNT; i++) {
                    while (currentLetter != letter) {
                        monitor.wait();
                    }
                    Thread.sleep(new Random().nextInt(1000)); // для теста честности порядка
                    System.out.print(letter);
                    currentLetter = Letter.next(currentLetter);
                    monitor.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}