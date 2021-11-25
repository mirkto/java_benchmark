package ru.tuanviet.javabox;

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
        System.out.println("da " + m.getName());
    }

    public void printResult() {
        String outPut = "[Test " + id + (con ? "PASSED" : "FAILED") + "]";
        System.out.println(outPut);
    }

}
