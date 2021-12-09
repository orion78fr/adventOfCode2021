package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Stream;

public class Day02 {
    @Part1
    public static long part1(List<String[]> input) {
        long hPos = 0;
        long depth = 0;

        for (String[] s : input) {
            long dist = Long.parseLong(s[1]);

            switch (s[0]) {
                case "forward" -> hPos += dist;
                case "down" -> depth += dist;
                case "up" -> depth -= dist;
            }
        }

        return hPos * depth;
    }

    @Part2
    public static long part2(List<String[]> input) {
        long aim = 0;
        long hPos = 0;
        long depth = 0;

        for (String[] s : input) {
            long dist = Long.parseLong(s[1]);

            switch (s[0]) {
                case "down" -> aim += dist;
                case "up" -> aim -= dist;
                case "forward" -> {
                    hPos += dist;
                    depth += (aim * dist);
                }
            }
        }

        return hPos * depth;
    }

    @InputParser
    public static List<String[]> parse(Stream<String> stream) {
        return stream.map(x -> x.split(" ")).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day02.txt"));
        List<String[]> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
