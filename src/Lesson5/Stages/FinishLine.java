package Lesson5.Stages;

import Lesson5.Players.Car;

import java.util.concurrent.CountDownLatch;


public class FinishLine extends Stage {

    private CountDownLatch countdown;
    private volatile boolean isFirstRun = true;



    public FinishLine(CountDownLatch countdown) {
        this.countdown = countdown;
    }
    @Override
    public void go(Car car) {
        if (isFirstRun) {
            isFirstRun = false;
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Победил автомобиль: " + car.getName());
        }
        countdown.countDown();
        try {
            countdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
