package Lesson5.Game;

import Lesson5.Stages.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

// Гонка
public class Race {
    private ArrayList<Stage> stages;
    private CountDownLatch countdown;

    public ArrayList<Stage> getStages() { return stages; }
    public Race(CountDownLatch countdown, Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.countdown = countdown;
    }

    public CountDownLatch getCountdown() {
        return countdown;
    }
}