package ru.tuanviet.javabox;

import java.util.AbstractMap.SimpleEntry;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SuperBenchmark {
    String startingTime;
    List<BenchTest> tests;

    public SuperBenchmark() {
        tests = new ArrayList<>();
    }

    public void benchmark(List<Class<?>> classes) {
        if (classes == null) {
            throw new IllegalArgumentException("Null parameter");
        }
        List<SimpleEntry<Class<?>, Method>> methods = new ArrayList<>();
        for (Class<?> cl : classes) {
            for (Method m : cl.getMethods()) {
                if (m.isAnnotationPresent(Benchmark.class)) {
                    methods.add(new SimpleEntry<>(cl, m));
                }
            }
        }
        if (methods.isEmpty()) {
            System.out.println("Benchmark annotation - Not found");
        } else {
            runBench(methods);
        }
    }

    private void runBench(List<SimpleEntry<Class<?>, Method>> methods) {
        startingTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date());
        System.out.println("Benchmark started at " + startingTime);
        for (SimpleEntry<Class<?>, Method> pair : methods) {
            tests.add(new BenchTest(pair.getKey(), pair.getValue()));
        }
    }

}
