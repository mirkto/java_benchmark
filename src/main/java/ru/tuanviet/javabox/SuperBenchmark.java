package ru.tuanviet.javabox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        List<Method> methods = new ArrayList<>();
        for (Class<?> cl : classes) {
            for (Method m : cl.getMethods()) {
                if (m.isAnnotationPresent(Benchmark.class)) {
                    methods.add(m);
                    try {
                        m.invoke(cl.getDeclaredConstructor().newInstance());
                    } catch (IllegalAccessException |
                            InvocationTargetException |
                            InstantiationException |
                            NoSuchMethodException e) {
                        e.printStackTrace();
                    }
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
        startingTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS").format(new Date());
        System.out.println("Benchmark started at " + startingTime);
        for (Method m : methods) {
            System.out.println();
            System.out.println("da " + m.getName());
            System.out.println(m.getParameterCount());
            tests.add(new BenchTest(m));
        }
    }

    private String representMethodNameForTest(Method m) {
        String methodName = m.toString();
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
        for (String w : methodName.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {

            rename.append(w).append(" ");
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
