package Lesson1.Task3.Boxes;

import Lesson1.Task3.Fruits.Fruit;

import java.util.ArrayList;

public class Box<E extends Fruit> implements Weightable {
    private ArrayList<E> fruits;

    public Box() {
        fruits = new ArrayList<>();
    }

    public ArrayList<E> getFruits() {
        return fruits;
    }

    public void add(E fruit) { fruits.add(fruit); }

    public double getWeight() {
        double weight = 0;
        for (E item: fruits) {
            weight += item.getWeight();
        }

        return weight;
    }

    public boolean compare(Box<?> box) {
        return this.getWeight() == box.getWeight();
    }

    public void shift(Box<E> box) {
        box.getFruits().addAll(fruits);
        fruits.clear();
    }


}
