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
    private Boolean con;
    int count;
    int repeats;
    int timeout;

    public BenchTest(Class<?> cl, Method m) {
        id = UUID.randomUUID();
        this.cl = cl;
        this.m = m;
        con = false;
        count = 0;
        testName = representMethodNameForTest(m);
        startBench();
        printResult();
    }

    private void startBench() {
        Benchmark ann = m.getAnnotation(Benchmark.class);
        if(ann != null) {
            repeats = ann.repeats();
            timeout = ann.timeout();
            for (; count < repeats; ++count) {
                try {
                    m.invoke(cl.getDeclaredConstructor().newInstance());
                } catch (IllegalAccessException |
                        InvocationTargetException |
                        InstantiationException |
                        NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if(count == repeats) {
                con = true;
            }
        }

    }

    public void printResult() {
        System.out.println("\n[Test " + id + (con ? " PASSED" : " FAILED") + "]");
        System.out.println("> " + testName);
        System.out.println("Repeats: " + count + "/" + repeats);
        System.out.println("Timeout: " + timeout + "ms");
        System.out.println("Min: " + 0 + "ms");
        System.out.println("Avg: " + 0 + "ms");
        System.out.println("Max: " + 0 + "ms");
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
    }

}
