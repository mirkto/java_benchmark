package ru.tuanviet.javabox;

import java.lang.reflect.Method;
import java.util.List;

public class SuperBenchmark {

    public SuperBenchmark() {
    }

    public void benchmark(List<Class<?>> classes) {
        if (classes == null) {
            throw new IllegalArgumentException("Null parameter");
        }

        for (Class<?> cl : classes) {
            //System.out.println("    class - " + cl.getSimpleName());
            for (Method m : cl.getMethods()) {
                if (m.isAnnotationPresent(Benchmark.class)) {
                    runBench(m);
                } else {
                    //System.out.println("net " + m.getName());
                }
            }
        }
    }

    private void runBench(Method m) {
        System.out.println("Benchmark started at ");
        System.out.println("da " + m.getName());
        System.out.println(m.getParameterCount());
    }
}
