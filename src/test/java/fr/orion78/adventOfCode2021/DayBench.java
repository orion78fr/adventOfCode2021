package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

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
@Fork(value = 1, jvmArgsAppend = {"-server", "-disablesystemassertions"})
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 2, time = 5)
public class DayBench {
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
    public Object bench() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(null, input);
    }

    private static class FastTest {
        public static void main(String[] args) throws RunnerException {
            Options opt = new OptionsBuilder()
                    .include(DayBench.class.getCanonicalName())
                    //.param("className", Day05.class.getCanonicalName())
                    //.param("methodName", "part2", "part2v2")
                    .param("className", Day21.class.getCanonicalName())
                    .param("methodName", "part1", "part2")
                    //.forks(1)
                    //.warmupIterations(1).warmupTime(TimeValue.seconds(5))
                    //.measurementIterations(2).warmupTime(TimeValue.seconds(5))
                    .forks(5)
                    .warmupIterations(2)
                    .measurementIterations(3)
                    .build();
            new Runner(opt).run();
        }
    }

    public static void main(String[] args) throws RunnerException, ClassNotFoundException {
        List<RunResult> runs = new ArrayList<>();

        List<Class<?>> classes = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("fr.orion78.adventOfCode2021").scan()) {
            ClassInfoList allClasses = scanResult.getClassesWithAnnotation(Day.class);
            List<String> names = allClasses.getNames();

            for (String name : names) {
                classes.add(Class.forName(name));
            }
        }

        for (Class<?> clazz : classes) {
            System.out.println("Class : " + clazz.getSimpleName());

            String[] methods = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(Part1.class) != null || m.getAnnotation(Part2.class) != null)
                    .map(Method::getName)
                    .toArray(String[]::new);

            Options opt = new OptionsBuilder()
                    .include(DayBench.class.getCanonicalName())
                    .verbosity(VerboseMode.SILENT)
                    .param("className", clazz.getCanonicalName())
                    .param("methodName", methods)
                    .build();

            runs.addAll(new Runner(opt).run());
        }

        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(0);

        for (RunResult run : runs) {
            BenchmarkResult res = run.getAggregatedResult();
            Result<?> primary = res.getPrimaryResult();

            System.out.println(res.getParams().getParam("className") + "\t"
                    + res.getParams().getParam("methodName") + "\t"
                    + f.format(primary.getScore()) + " ± " + f.format(primary.getScoreError()) + " " + primary.getScoreUnit());
        }
    }
}
