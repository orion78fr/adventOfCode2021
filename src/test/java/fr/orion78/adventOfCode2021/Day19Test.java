package fr.orion78.adventOfCode2021;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Stream;

public class Day19Test {
    @Test
    public void testParse() {
        Stream<String> input = Stream.of("--- scanner 0 ---",
                "0,2,0",
                "4,1,0",
                "3,3,0",
                "",
                "--- scanner 1 ---",
                "-1,-1,0",
                "-5,0,0",
                "-2,1,0");

        Assert.assertEquals(Day19.parse(input), List.of(
                new Day19.Scanner(0, List.of(
                        new Day19.Point(0,2,0),
                        new Day19.Point(4,1,0),
                        new Day19.Point(3,3,0)
                )),
                new Day19.Scanner(1, List.of(
                        new Day19.Point(-1,-1,0),
                        new Day19.Point(-5,0,0),
                        new Day19.Point(-2,1,0)
                ))
        ));
    }
}
