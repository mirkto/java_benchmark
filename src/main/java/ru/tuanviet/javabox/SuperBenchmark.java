package ru.tuanviet.javabox;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SuperBenchmark {

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
        System.out.println("Benchmark started at ");
        for (Method m : methods) {
            System.out.println("da " + m.getName());
            System.out.println(m.getParameterCount());
        }
    }
}
