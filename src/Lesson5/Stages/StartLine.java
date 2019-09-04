package Lesson5.Stages;

import Lesson5.Players.Car;

import java.util.concurrent.CountDownLatch;

public class StartLine extends Stage {

    private CountDownLatch countdown;


    public StartLine(CountDownLatch countdown) {
        this.countdown = countdown;
    }
    @Override
    public void go(Car car) {
        countdown.countDown();
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
