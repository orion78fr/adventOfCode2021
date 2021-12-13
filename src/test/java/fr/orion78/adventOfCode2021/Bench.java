package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgsAppend = {"-server", "-disablesystemassertions"})
@Warmup(iterations = 1, time = 10)
@Measurement(iterations = 3, time = 5)
public class Bench {
    private static final List<List<String>> inputs = new ArrayList<>();
    private static final List<Method> inputParsers = new ArrayList<>();
    private static final List<Method> part1BestMethod = new ArrayList<>();
    private static final List<Method> part2BestMethod = new ArrayList<>();

    @Setup(Level.Trial)
    public void setup() throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("fr.orion78.adventOfCode2021").scan()) {
            ClassInfoList allClasses = scanResult.getClassesWithAnnotation(Day.class);
            List<String> names = allClasses.getNames();

            for (String name : names) {
                classes.add(Class.forName(name));
            }
        }

        for (var clazz : classes) {
            try (var r = new BufferedReader(new FileReader(clazz.getSimpleName().toLowerCase() + ".txt"))) {
                inputs.add(r.lines().toList());
            }

            inputParsers.add(Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(InputParser.class) != null)
                    .findAny()
                    .get());

            part1BestMethod.add(Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(Part1.class) != null)
                    .max(Comparator.comparing(m -> m.getAnnotation(Part1.class).optLevel()))
                    .get());

            part2BestMethod.add(Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(Part2.class) != null)
                    .max(Comparator.comparing(m -> m.getAnnotation(Part2.class).optLevel()))
                    .get());
        }
    }

    @Benchmark
    public void fullTest() throws InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < inputs.size(); i++) {
            List<String> input = inputs.get(i);
            Method inputParser = inputParsers.get(i);
            Method part1 = part1BestMethod.get(i);
            Method part2 = part2BestMethod.get(i);

            Object parsedInput = inputParser.invoke(null, input.stream());
            part1.invoke(null, parsedInput);
            part2.invoke(null, parsedInput);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Bench.class.getCanonicalName())
                .build();

        new Runner(opt).run();
    }
}
