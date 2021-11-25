package ru.tuanviet.javabox;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperBenchmarkTest {
    final PrintStream defaultSystemOut = System.out;
    final PrintStream defaultSystemErr = System.err;
    List<Class<?>> sutList = new ArrayList<>();
    SuperBenchmark sutBench = new SuperBenchmark();
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private void captureSystemOut() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private void releaseSystemOut() {
        System.setOut(defaultSystemOut);
        System.setErr(defaultSystemErr);
    }

    private String getLineByNumber(String output, int num) {
        String[] lines = output.split("\n");

        if (num > 0 && num <= lines.length) {
            return lines[num - 1];
        }
        return null;
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

        String actual = getLineByNumber(outContent.toString(), 1);
        assertThat(actual).startsWith("Benchmark started at ");
    }

    @Test
    public void shouldSeparateEachTestWithNewLine() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 2);
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldHaveMinimumExecutionTimeOn7thLine() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 7);
        assertThat(actual).startsWith("Min: ");
    }

    @Test
    public void shouldHaveAverageExecutionTimeOn8thLine() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 8);
        assertThat(actual).startsWith("Avg: ");
    }

    @Test
    public void shouldHaveMaximumExecutionTimeOn9thLine() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 9);
        assertThat(actual).startsWith("Max: ");
    }

}
