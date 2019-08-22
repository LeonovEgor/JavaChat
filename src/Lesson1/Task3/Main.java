package Lesson1.Task3;

// 3. Большая задача:
//   +Есть классы Fruit -> Apple, Orange (больше фруктов не надо);
//   +Класс Box, в который можно складывать фрукты. Коробки условно сортируются по типу фрукта, поэтому
//     в одну коробку нельзя сложить и яблоки, и апельсины;
//   +Для хранения фруктов внутри коробки можно использовать ArrayList;
//   +Сделать метод getWeight(), который высчитывает вес коробки, зная количество фруктов и вес одного фрукта
//     (вес яблока – 1.0f, апельсина – 1.5f. Не важно, в каких это единицах);
//   +Внутри класса Коробка сделать метод compare, который позволяет сравнить текущую коробку с той,
//     которую подадут в compare в качестве параметра, true – если она равны по весу, false – в противном случае
//     (коробки с яблоками мы можем сравнивать с коробками с апельсинами);
//   +Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую
//     (помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами).
//     Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты,
//     которые были в этой коробке;
//   +Не забываем про метод добавления фрукта в коробку.

import Lesson1.Task3.Boxes.Box;
import Lesson1.Task3.Fruits.Apple;
import Lesson1.Task3.Fruits.Orange;

public class Main {
    public static void main(String[] args) {
        Box<Apple> appleBox1 = new Box<>();
        appleBox1.add(new Apple());
        appleBox1.add(new Apple());
        appleBox1.add(new Apple());
        System.out.println("Коробка 1 весит " + appleBox1.getWeight());

        Box<Apple> appleBox2 = new Box<>();
        appleBox2.add(new Apple());
        appleBox2.add(new Apple());
        System.out.println("Коробка 2 весит " + appleBox2.getWeight());

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());
        System.out.println("Коробка 3 весит " + orangeBox.getWeight());

        System.out.println("Пересыпаем из коробки 2 в коробку 1");
        appleBox2.shift(appleBox1);
        System.out.println("Коробка 1 теперь весит " + appleBox1.getWeight());

        System.out.println("Коробка 1 и Коробка 3 весят одинаково?: " + appleBox1.compare(orangeBox));
    }
}
