package Lesson5;

import Lesson5.Game.Race;
import Lesson5.Players.Car;
import Lesson5.Stages.Road;
import Lesson5.Stages.StartLine;
import Lesson5.Stages.Tunnel;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int CARS_COUNT = 4;
    private static final int TRAFFIC_CAPACITY = CARS_COUNT/2;
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 80;

    public static void main(String[] args) {
        int roadLength1 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;
        int roadLength2 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;
        int tunnelLength1 = new Random().nextInt(MAX_LENGTH) + MIN_LENGTH;

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        StartLine startLine = new StartLine(CARS_COUNT);
        Race race = new Race(
                startLine,
                new Road(roadLength1),
                new Tunnel(TRAFFIC_CAPACITY, tunnelLength1),
                new Road(roadLength2));


        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        CountDownLatch countdown = new CountDownLatch(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }


        //System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
