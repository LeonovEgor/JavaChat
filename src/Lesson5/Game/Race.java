package Lesson5.Game;

import Lesson5.Players.Car;
import Lesson5.Stages.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

// Гонка
public class Race {
    private ArrayList<Stage> stages;

    private final CountDownLatch countdown;
    private final CountDownLatch finish;
    private volatile boolean isFirstCar = true;
    public boolean isFirst() { return isFirstCar; }


    public CountDownLatch getCountdown() {
        return countdown;
    }
    public CountDownLatch getFinish() { return finish; }

    public ArrayList<Stage> getStages() { return stages; }

    public Race(int carCount, Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.countdown = new CountDownLatch(carCount+1);
        this.finish = new CountDownLatch(carCount);

        new Thread( () -> { // сообщение начала
            try {
                while(countdown.getCount() > 1) {
                    Thread.sleep(100);
                }
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                countdown.countDown(); //пуск
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> { // сообщение окончания
            try {

                finish.await();
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void start(Car[] cars) {
        for (Car car : cars) new Thread(car).start();
    }

    public void printWin(String name) {
        isFirstCar = false;
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Победил автомобиль: " + name);
    }
}