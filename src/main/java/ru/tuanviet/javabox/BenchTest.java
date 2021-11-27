package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

public class BenchTest {
    private final String testName;
    private final UUID id;
    private final Class<?> cl;
    private final Method m;
    private Boolean cond;
    int countOfRepeat;
    int repeats;
    long timeout;
    long min;
    long avg;
    long max;

    public BenchTest(Class<?> cl, Method m) {
        id = UUID.randomUUID();
        if (cl == null) {
            throw new IllegalArgumentException("Null first arg");
        }
        this.cl = cl;
        if (m == null) {
            throw new IllegalArgumentException("Null second arg");
        }
        this.m = m;
        cond = false;
        countOfRepeat = 0;
        min = max = -1;
        avg = 0;
        testName = representMethodNameForTest(m);
        startBench();
        printResult();
    }

    private void startBench() {
        Benchmark ann = m.getAnnotation(Benchmark.class);
        if(ann == null) {
            throw new IllegalArgumentException("Method without required annotation");
        }
        repeats = ann.repeats();
        timeout = ann.timeout();
//        long timeToStartTest = System.currentTimeMillis();
        while (countOfRepeat < repeats) {
            long timeToStartRepeat = System.currentTimeMillis();
            try {
                m.invoke(cl.getDeclaredConstructor().newInstance());
            } catch (IllegalAccessException |
                    InvocationTargetException |
                    InstantiationException |
                    NoSuchMethodException e) {
                e.printStackTrace();
            }
            ++countOfRepeat;
            long time = System.currentTimeMillis() - timeToStartRepeat;
            min = min == -1 ? time : Math.min(min, time);
            max = max == -1 ? time : Math.max(max, time);
            avg += time;
            if(time > timeout) {
                break;
            }
        }
        avg = Math.round((double) avg / (countOfRepeat == 0 ? 1 : countOfRepeat));
        if(countOfRepeat == repeats) {
            cond = true;
        }
    }

    public void printResult() {
        System.out.println("\n[Test " + id + (cond ? " PASSED" : " FAILED") + "]");
        System.out.println("> " + testName);
        System.out.println("Repeats: " + countOfRepeat + "/" + repeats);
        System.out.println("Timeout: " + timeout + "ms");
        System.out.println("Min: " + min + "ms");
        System.out.println("Avg: " + avg + "ms");
        System.out.println("Max: " + max + "ms");
    }

    private String representMethodNameForTest(Method m) {
        String methodName = m.getName();
        String result;

        if (methodName.contains("_")) {
            result =  convertSnakeCaseToString(methodName);
        } else {
            result =  convertCamelCaseToString(methodName);
        }
        return capitalizeStringAndRemoveShouldWord(result);
    }

    private String capitalizeStringAndRemoveShouldWord(String result) {
        if (result.startsWith("should")) {
            result = result.substring(7);
        }
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }

    private String convertCamelCaseToString(String methodName) {
        StringBuilder rename = new StringBuilder();
        for (String word : methodName.split("(?<!(^|[A-Z0-9]))(?=[A-Z0-9])|(?<!(^|[^A-Z]))(?=[0-9])|(?<!(^|[^0-9]))(?=[A-Za-z])|(?<!^)(?=[A-Z][a-z])")) {
            rename.append(word).append(" ");
        }
        return rename.toString().toLowerCase(Locale.ROOT).trim();
    }

    private String convertSnakeCaseToString(String methodName) {
        StringBuilder result = new StringBuilder();
        for (String word : methodName.toLowerCase(Locale.ROOT).split("_")) {
            result.append(word).append(" ");
        }
        return result.toString().trim();
//        return methodName.toLowerCase(Locale.ROOT).replace("_"," ");
    }

}
