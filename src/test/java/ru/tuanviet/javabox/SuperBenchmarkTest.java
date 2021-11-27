package ru.tuanviet.javabox;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
    public void shouldFindAndBenchThreeMethods() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);
        sutList.add(BenchMarks2.class);
        sutList.add(BenchMarks3.class);

        sutBench.benchmark(sutList);
        String output = outContent.toString();
        int newLines = 0;
        int i = 1;
        String line = getLineByNumber(output, i);
        while (line != null) {
            if (line.isEmpty()) {
                newLines++;
            }
            line = getLineByNumber(output, ++i);
        }

        assertThat(newLines).isEqualTo(3);
    }

    @Test
    public void shouldHaveTestNameOn4thLine() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 4);
        assertThat(actual).startsWith("> ");
    }

    @Test
    public void shouldConvertSnakeCaseToMethodName() {
        captureSystemOut();
        sutList.add(BenchMarks1.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 4);
        assertThat(actual).isEqualTo("> Add 10000 numbers in 1 second");
    }

    @Test
    public void shouldConvertCamelCaseToMethodName() {
        captureSystemOut();
        sutList.add(BenchMarks3.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 4);
        assertThat(actual).isEqualTo("> Add 50 numbers in 10 milliseconds");
    }

    @Test
    public void shouldHaveRepeatsParameterOn5thLine() {
        captureSystemOut();
        sutList.add(BenchMarks3.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 5);
        assertThat(actual).startsWith("Repeats: ");
    }

    @Test
    public void shouldHaveTimeoutParameterOn6thLine() {
        captureSystemOut();
        sutList.add(BenchMarks3.class);

        sutBench.benchmark(sutList);

        String actual = getLineByNumber(outContent.toString(), 6);
        assertThat(actual).startsWith("Timeout: ");
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
