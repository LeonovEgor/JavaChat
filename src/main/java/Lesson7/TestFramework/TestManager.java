package Lesson7.TestFramework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestManager {

    public static void start(Object  testObj) {

        Class testClass = testObj.getClass();
        Method[] methods = testClass.getDeclaredMethods();

        List<Method> testMethodList = getTestMethod(methods);

        runTests(testObj, testMethodList);
    }

    private static List<Method> getTestMethod(Method[] methods) {
        Method before = null;
        Method after = null;
        List<Method> testMethodList = new ArrayList<>();

        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) {
                if (before == null) before = method;
                else throw new RuntimeException("Метод c анотацией Before может быть только один!");
            }
            else if (method.getAnnotation(AfterSuite.class) != null) {
                if (after == null) after = method;
                else throw new RuntimeException("Метод c анотацией Before может быть только один!");
            }
            else if (method.getAnnotation(Test.class) != null) {
                System.out.println("Найден тестовый метод " + method.getName());
                testMethodList.add(method);
            }
        }

        System.out.println("Методы до сортировки " + testMethodList.toString());
        testMethodList.sort((a, b) -> {
            Test testA = a.getAnnotation(Test.class);
            Test testB = b.getAnnotation(Test.class);
            return Integer.compare(testA.level(), testB.level());
        });

        testMethodList.add(0, before);
        testMethodList.add(testMethodList.size(), after);

        System.out.println("Методы после сортировки " + testMethodList.toString());
        return testMethodList;
    }

    private static void runTests(Object testObj, List<Method> testMethodList) {
        try {
            for (Object method: testMethodList) {
                ((Method)method).invoke(testObj);

            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}


