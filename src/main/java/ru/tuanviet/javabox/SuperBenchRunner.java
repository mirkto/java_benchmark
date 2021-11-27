package ru.tuanviet.javabox;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import java.util.List;

public class SuperBenchRunner extends Runner {
    private final Class<?> cl;

    public SuperBenchRunner(Class<?> cl) {
        super();
        this.cl = cl;
    }

    @Override
    public Description getDescription() {
        return Description
                .createTestDescription(cl, "SuperBenchRunner description");
    }

    @Override
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from SuperBenchRunner: " + cl.getName());
        new SuperBenchmark().benchmark(List.of(cl));
    }
}
