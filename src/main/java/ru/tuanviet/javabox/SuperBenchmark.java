package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuperBenchmark {

    String startingTime;
    public SuperBenchmark() {
    }

    public void benchmark(List<Class<?>> classes) {
        if (classes == null) {
            throw new IllegalArgumentException("Null parameter");
        }

        List<Method> methods = new ArrayList<>();
        for (Class<?> cl : classes) {
            for (Method m : cl.getMethods()) {
                if (m.isAnnotationPresent(Benchmark.class)) {
                    methods.add(m);
                    try {
                        m.invoke(cl.getDeclaredConstructor().newInstance());
                    } catch (IllegalAccessException |
                            InvocationTargetException |
                            InstantiationException |
                            NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (methods.isEmpty()) {
            System.out.println("Benchmark annotation - Not found");
        } else {
            runBench(methods);
        }
    }

    private void runBench(List<Method> methods) {
        startingTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date());
        System.out.println("Benchmark started at " + startingTime);
        for (Method m : methods) {
            System.out.println("da " + m.getName());
            System.out.println(m.getParameterCount());
        }
    }

    private void runMethod(Method m) {

    }
}
