package ru.tuanviet.javabox;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SuperBenchRunner.class)
public class SuperBenchRunnerTest {

    @Test
    public void testAddition() {
        System.out.println("in testAddition");
    }

    @Benchmark(repeats = 10_000, timeout = 1_000)
    public void should_add_10000_numbers_in_1_second() {
        int sum = App.add(10, 15);
    }

    @Benchmark(repeats = 10, timeout = 100)
    public void should_add_10_numbers_in_100_milliseconds() {
        int sum = App.add(10, 15);
        App.sleep(1);
    }

    @Benchmark(repeats = 50, timeout = 10)
    public void shouldAdd50NumbersIn10Milliseconds() {
        int sum = App.add(10, 15);
        App.sleep(7);
    }

    @Benchmark(repeats = 10, timeout = 10)
    public void shouldAdd10NumbersIn10Milliseconds() {
        int sum = App.add(10, 15);
        App.sleep(9);
    }
}