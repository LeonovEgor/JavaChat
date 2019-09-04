package Lesson5.Stages;

import Lesson5.Players.Car;

import java.util.concurrent.Semaphore;

// Туннель
public class Tunnel extends Stage {
    private int trafficCapacity;
    private Semaphore semaphore; // Будет задерживать автомобили больше допустимого количества у въезда в туннель

    public Tunnel(int trafficCapacity, int length, Semaphore semaphore) {
        this.length = length;
        this.description = "Тоннель " + length + " метров";
        this.trafficCapacity = trafficCapacity;

        this.semaphore = semaphore;
    }

    @Override
    public void go(Car car) {
        try {
            try {
                System.out.println(car.getName() + " готовится к этапу(ждет): " + description);
                System.out.println("В туннеле свободно для " + semaphore.availablePermits() + " машин");
                semaphore.acquire();

                System.out.println(car.getName() + " начал этап: " + description);
                Thread.sleep(length / car.getSpeed() * 1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(car.getName() + " закончил этап: " + description);
                semaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}