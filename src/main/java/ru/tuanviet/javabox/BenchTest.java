package ru.tuanviet.javabox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.UUID;

public class BenchTest {
    private final UUID id;
    private final Method m;
    private Boolean con;

    public BenchTest(Method m) {
        id = UUID.randomUUID();
        con = false;
        this.m = m;
        startBench();
        printResult();
    }

    public void startBench() {
        System.out.println("\n - " + m.getName());

        Benchmark ann = m.getAnnotation(Benchmark.class);
        if(ann != null) {
            int repeats = ann.repeats();
            int timeout = ann.timeout();
            String outPut = "-- repeats = " + repeats + " timeout = " + timeout;
            System.out.println(outPut);
        }

    }

    public void printResult() {
        String outPut = "[Test " + id + (con ? " PASSED" : " FAILED") + "]";
        System.out.println(outPut);
    }

}
