package ru.tuanviet.javabox;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SuperBenchmarkTest {

    List<Class<?>> sutList = new ArrayList<>();
    SuperBenchmark sutBench = new SuperBenchmark();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullArgument() {
        new SuperBenchmark().benchmark(null);
    }

    @Test
    public void tets() {
        App test = new App();
        sutList.add(App.class);
       sutBench.benchmark(sutList);
    }
}
