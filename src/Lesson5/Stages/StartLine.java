package Lesson5.Stages;

import Lesson5.Players.Car;

import java.util.concurrent.CountDownLatch;

public class StartLine extends Stage {

    private CountDownLatch countdown;

    public StartLine(int carCount) {
        countdown = new CountDownLatch(carCount);
    }
    @Override
    public void go(Car car) {
        countdown.countDown();
    }
}
