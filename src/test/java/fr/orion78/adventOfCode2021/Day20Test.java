package fr.orion78.adventOfCode2021;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day20Test {
    public List<String> enhancement = List.of(
            "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
                    "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
                    ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
                    ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
                    ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
                    "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
                    "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#",
            "",
            "#..#.",
            "#....",
            "##..#",
            "..#..",
            "..###");

    @Test
    public void testParse() {
        Day20.Input input = Day20.parse(enhancement.stream());

        Assert.assertEquals(input.enhancement().size(), 512);
        Assert.assertEquals(input.litPixels().size(), 10);

        Assert.assertTrue(input.litPixels().contains(new Day20.Point(0, 0)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(3, 0)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(0, 1)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(0, 2)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(1, 2)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(4, 2)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(2, 3)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(2, 4)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(3, 4)));
        Assert.assertTrue(input.litPixels().contains(new Day20.Point(4, 4)));

        Assert.assertFalse(input.enhancement().get(0));
        Assert.assertFalse(input.enhancement().get(1));
        Assert.assertTrue(input.enhancement().get(2));
    }

    @Test
    public void testStep() {
        Day20.Input input = Day20.parse(enhancement.stream());

        Assert.assertEquals(input.litPixels().size(), 10);

        Set<Day20.Point> step1 = Day20.step(input.enhancement(), new HashSet<>(input.litPixels()));
        Assert.assertEquals(step1.size(), 24);

        Set<Day20.Point> step2 = Day20.step(input.enhancement(), step1);
        Assert.assertEquals(step2.size(), 35);

        Set<Day20.Point> doubleStep = Day20.doubleStep(input.enhancement(), new HashSet<>(input.litPixels()));
        Assert.assertEquals(doubleStep.size(), 35);
    }
}
