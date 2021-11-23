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

        for (Class<?> cl : classes) {
            for (Method m : cl.getMethods()) {
                if (m.isAnnotationPresent(Benchmark.class)) {
                    System.out.println("da " + m.getName());
                } else {
                    System.out.println("net " + m.getName());
                }
            }

        }
    }
}
