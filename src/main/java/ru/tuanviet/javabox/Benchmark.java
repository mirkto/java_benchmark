package ru.tuanviet.javabox;

import java.lang.annotation.*;

@Target(value= ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Benchmark {

    int repeats();

    int timeout();
}
