package ru.tuanviet.javabox;

import java.util.Arrays;

public class App {
    public static void main(String... args) {
        new SuperBenchmark().benchmark(
                Arrays.asList(BenchMarks1.class, BenchMarks2.class)
        );
    }

    public static int add(int x, int y) {
        return x + y;
    }

}

class BenchMarks1 {
    @Benchmark(repeats = 10_000, timeout = 1_000)
    public void should_add_10000_numbers_in_1_second() {
        App.add(10, 15);
    }
}

class BenchMarks2 {
    @Benchmark(repeats = 10, timeout = 100)
    public void should_add_10_numbers_in_100_milliseconds() {
        App.add(10, 15);
    }
}

// -- OUTPUT ------------------------------------------------
// Benchmark started at 2021.11.07 14:34:17.456
//
// [Test 1b6cefae-a333-4074-a4c1-b7ca35f9de3c PASSED]
// > Add 10000 numbers in 1 second
// Repeats: 10000/10000
// Timeout: 1000ms
// Min: 12ms
// Avg: 23ms
// Max: 67ms
//
// [Test 98ebe708-1c73-46ea-8c8c-05e1d49dfadb FAILED]
// > Add 10 numbers in 100 milliseconds
// Repeats: 6/10
// Timeout: 100ms
// Min: 14ms
// Avg: 18ms
// Max: 171ms
