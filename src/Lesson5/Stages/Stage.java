package Lesson5.Stages;

import Lesson5.Players.Car;

// Этап
public abstract class Stage {
    int length;
    String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car car);
}