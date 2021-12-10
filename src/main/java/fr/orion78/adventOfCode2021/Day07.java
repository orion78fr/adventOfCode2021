package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day07 {
    @Part1
    public static long part1(List<Integer> l) {
        return IntStream.range(l.get(0), l.get(l.size() - 1) + 1)
                .map(i -> l.stream()
                        .mapToInt(x -> Math.abs(x - i))
                        .sum())
                .min().getAsInt();
    }

    @Part1(optLevel = 1)
    public static long part1Median(List<Integer> l) {
        int median = (l.get(l.size() / 2 - 1) + l.get(l.size() / 2)) / 2;
        return l.stream()
                .mapToInt(x -> Math.abs(x - median))
                .sum();
    }

    @Part2
    public static long part2(List<Integer> l) {
        return IntStream.range(l.get(0), l.get(l.size() - 1) + 1)
                .map(i -> l.stream()
                        .mapToInt(x -> {
                            int dist = Math.abs(x - i);
                            return dist * (dist + 1) / 2;
                        })
                        .sum())
                .min().getAsInt();
    }

    @InputParser
    public static List<Integer> parse(Stream<String> stream) {
        return stream
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(Integer::parseInt)
                .sorted()
                .toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day07.txt"));
        List<Integer> l = parse(br.lines());

        System.out.println(part1Median(l));
        System.out.println(part2(l));
    }
}
