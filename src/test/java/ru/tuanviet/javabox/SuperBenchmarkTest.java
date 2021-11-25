package ru.tuanviet.javabox;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperBenchmarkTest {
    List<Class<?>> sutList = new ArrayList<>();
    SuperBenchmark sutBench = new SuperBenchmark();

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    final PrintStream defaultSystemOut = System.out;
    final PrintStream defaultSystemErr = System.err;

    public void captureSystemOut() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void releaseSystemOut() {
        System.setOut(defaultSystemOut);
        System.setErr(defaultSystemErr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnNullArgument() {
        new SuperBenchmark().benchmark(null);
    }

    @Test
    public void shouldPrintNotFoundMessage() {
        captureSystemOut();
        sutList.add(App.class);
        sutList.add(Object.class);
        sutList.add(String.class);

        sutBench.benchmark(sutList);
        String actual = outContent.toString();

        assertThat(actual).startsWith("Benchmark annotation - Not found");
    }

    @Test
    public void shouldPrintStartingMessageWhenBenchmarkStart() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);

        sutBench.benchmark(sutList);
        String actual = outContent.toString();

        assertThat(actual).startsWith("Benchmark started at ");
    }
}
