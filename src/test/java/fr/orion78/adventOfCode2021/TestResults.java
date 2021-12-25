package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TestResults {
    @DataProvider
    public Object[][] expectedResults() {
        return new Object[][]{
                new Object[]{Day01.class, 1316L, 1344L},
                new Object[]{Day02.class, 1660158L, 1604592846L},
                new Object[]{Day03.class, 2035764L, 2817661L},
                new Object[]{Day04.class, 8136L, 12738L},
                new Object[]{Day05.class, 7380L, 21373L},
                new Object[]{Day06.class, 352195L, 1600306001288L},
                new Object[]{Day07.class, 335330L, 92439766L},
                new Object[]{Day08.class, 519L, 1027483L},
                new Object[]{Day09.class, 475L, 1092012L},
                new Object[]{Day10.class, 370407L, 3249889609L},
                new Object[]{Day11.class, 1749L, 285L},
                new Object[]{Day12.class, 4495L, 131254L},
                new Object[]{Day13.class, 610L, "PZFJHRFZ"},
                new Object[]{Day14.class, 2602L, 2942885922173L},
                new Object[]{Day15.class, 447L, 2825L},
                new Object[]{Day16.class, 979L, 277110354175L},
                new Object[]{Day17.class, 6903L, 2351L},
                new Object[]{Day18.class, 3411L, 4680L},
                new Object[]{Day19.class, new Day19.Result(394L, 12304L), null},
                new Object[]{Day20.class, new Day20.Result(5479L, 19012L), null},
                new Object[]{Day21.class, 916083L, 49982165861983L},
                new Object[]{Day22.class, new Day22.Result(576028L, 1387966280636636L), null},
                // new Object[]{ Day23.class, 15111L, 47625L},
                // new Object[]{ Day24.class, 91897399498995L, 51121176121391L},
                new Object[]{Day25.class, 295L, null},

        };
    }

    @Test(dataProvider = "expectedResults")
    public void checkExpectedResults(Class<?> clazz, Object expectedPart1, Object expectedPart2) throws IOException, InvocationTargetException, IllegalAccessException {
        List<String> input;

        try (var r = new BufferedReader(new FileReader(clazz.getSimpleName().toLowerCase() + ".txt"))) {
            input = r.lines().toList();
        }

        Method inputParser = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getAnnotation(InputParser.class) != null)
                .findAny()
                .get();

        Object parsedInput = inputParser.invoke(null, input.stream());

        Method part1Method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getAnnotation(Part1.class) != null)
                .max(Comparator.comparing(m -> m.getAnnotation(Part1.class).optLevel()))
                .get();

        Assert.assertEquals(part1Method.invoke(null, parsedInput), expectedPart1);

        if (!part1Method.getAnnotation(Part1.class).bothParts()) {
            Method part2Method = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getAnnotation(Part2.class) != null)
                    .max(Comparator.comparing(m -> m.getAnnotation(Part2.class).optLevel()))
                    .get();

            Assert.assertEquals(part2Method.invoke(null, parsedInput), expectedPart2);
        }
    }
}
