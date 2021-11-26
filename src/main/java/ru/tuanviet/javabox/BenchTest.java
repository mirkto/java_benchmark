package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

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
        if (ann == null) {
            throw new IllegalArgumentException("Method without required annotation");
        }
        repeats = ann.repeats();
        timeout = ann.timeout();
        while (countOfRepeat < repeats) {
            long timeToStartRepeat = System.currentTimeMillis();
            invokeBench();
            ++countOfRepeat;
            long time = System.currentTimeMillis() - timeToStartRepeat;
            min = min == -1 ? time : Math.min(min, time);
            max = max == -1 ? time : Math.max(max, time);
            avg += time;
            if (time > timeout) {
                break;
            }
        }
        avg = Math.round((double) avg / (countOfRepeat == 0 ? 1 : countOfRepeat));
        if (countOfRepeat == repeats) {
            cond = true;
        }
    }

    private void invokeBench() {
        try {
            m.invoke(cl.getDeclaredConstructor().newInstance());
        } catch (IllegalAccessException |
                InvocationTargetException |
                InstantiationException |
                NoSuchMethodException e) {
            e.printStackTrace();
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
        String result = m.getName();
        String regexSnakeCase = "([a-z]+_+\\w+)+";
        String regexCamelCase = "([a-z]+[A-Z]+\\w+)+";

        if (Pattern.compile(regexSnakeCase).matcher(m.getName()).matches()) {
            result = m.getName().replace("_", " ");
        } else if (Pattern.compile(regexCamelCase).matcher(m.getName()).matches()) {
            result = splitCamelCase(result);
        }
        return capitalizeStringAndRemoveShouldWord(result);
    }

    private String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    private String capitalizeStringAndRemoveShouldWord(String result) {
        if (result.startsWith("should")) {
            result = result.substring(7);
        }
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }
}