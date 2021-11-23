package ru.tuanviet.javabox;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class SuperBenchmarkTest {
    List<Class<?>> sutList = new ArrayList<>();
    SuperBenchmark sutBench = new SuperBenchmark();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullArgument() {
        new SuperBenchmark().benchmark(null);
    }

    @Test
    public void shouldPrintStartingMessageWhenBenchmarkStart() {
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);
        sutBench.benchmark(sutList);
        String actual = outContent.toString();

        assertThat(actual).startsWith("Benchmark started at ");
    }
}
