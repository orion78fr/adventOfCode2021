package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 0, jvmArgsAppend = {"-server", "-disablesystemassertions"})
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 3)
public class DayBench {
    private static List<Class<?>> classes = List.of(Day01.class, Day02.class, Day03.class, Day04.class, Day05.class,
            Day06.class, Day07.class, Day08.class, Day09.class/*, Day10.class, Day11.class, Day12.class, Day13.class,
            Day14.class, Day15.class, Day16.class, Day17.class, Day18.class, Day19.class, Day20.class, Day21.class,
            Day22.class, Day23.class, Day24.class, Day25.class*/);

    @Param("NOT INITIALIZED")
    private String className;
    @Param("NOT INITIALIZED")
    private String methodName;

    private Class<?> clazz;
    private Method method;

    private Object input;

    @Setup(Level.Trial)
    public void setup() throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        clazz = Class.forName(className);

        Method parser = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getAnnotation(InputParser.class) != null)
                .findAny()
                .get();

        try (var r = new BufferedReader(new FileReader(clazz.getSimpleName().toLowerCase() + ".txt"))) {
            this.input = parser.invoke(null, r.lines());
        }
    }

    @Setup(Level.Iteration)
    public void setupMethod() {
        method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .findAny().get();
    }

    @Benchmark
    public void bench() throws InvocationTargetException, IllegalAccessException {
        method.invoke(null, input);
    }

    public static void main(String[] args) throws RunnerException {
        List<RunResult> runs = new ArrayList<>();

        for (Class<?> clazz : classes) {
            String[] methods = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(Part1.class) != null || m.getAnnotation(Part2.class) != null)
                    .map(Method::getName)
                    .toArray(String[]::new);

            Options opt = new OptionsBuilder()
                    .include(DayBench.class.getCanonicalName())
                    .param("className", clazz.getCanonicalName())
                    .param("methodName", methods)
                    .build();

            runs.addAll(new Runner(opt).run());
        }

        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(2);

        for (RunResult run : runs) {
            BenchmarkResult res = run.getAggregatedResult();
            Result primary = res.getPrimaryResult();

            System.out.println(res.getParams().getParam("className") + "\t"
                    + res.getParams().getParam("methodName") + "\t"
                    + f.format(primary.getScore()) + " ± " + f.format(primary.getScoreError()) + " " + primary.getScoreUnit());
        }
    }
}
