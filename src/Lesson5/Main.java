package Lesson5;

import Lesson5.Game.Race;
import Lesson5.Players.Car;
import Lesson5.Stages.FinishLine;
import Lesson5.Stages.Road;
import Lesson5.Stages.StartLine;
import Lesson5.Stages.Tunnel;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Main {
    private static final int CARS_COUNT = 4;
    private static final int TRAFFIC_CAPACITY = CARS_COUNT/2;
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 80;
    private static final CountDownLatch COUNTDOWN = new CountDownLatch(CARS_COUNT+1);
    private static final CountDownLatch FINISH = new CountDownLatch(CARS_COUNT);
    private static final Semaphore TUNNEL_SEMAPHORE = new Semaphore(TRAFFIC_CAPACITY);
    private static volatile boolean firstCar = true;

    public static void main(String[] args) throws InterruptedException {
        int roadLength1 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;
        int roadLength2 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;
        int tunnelLength1 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        StartLine startLine = new StartLine(COUNTDOWN);
        FinishLine finishLine = new FinishLine(FINISH);

        Race race = new Race(COUNTDOWN,
                startLine,
                new Road(roadLength1),
                new Tunnel(TRAFFIC_CAPACITY, tunnelLength1, TUNNEL_SEMAPHORE),
                new Road(roadLength2),
                finishLine);

        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        new Thread( () -> { // сообщение начала
            try {
                while(COUNTDOWN.getCount() > 1) {
                    Thread.sleep(100);
                }
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                COUNTDOWN.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread( () -> { // сообщение окончания
            try {
                FINISH.await();
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        for (Car car : cars) new Thread(car).start();
    }
}
