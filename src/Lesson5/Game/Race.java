package Lesson5.Game;

import Lesson5.Stages.Stage;

import java.util.ArrayList;
import java.util.Arrays;

// Гонка
public class Race {
    private ArrayList<Stage> stages;
    public ArrayList<Stage> getStages() { return stages; }
    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }
}