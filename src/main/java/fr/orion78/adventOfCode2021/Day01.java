package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Stream;

@Day
public class Day01 {
    @Part1
    public static long part1(List<Integer> input) {
        int increased = 0;
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i - 1) < input.get(i)) {
                increased++;
            }
        }
        return increased;
    }

    @Part2
    public static long part2(List<Integer> input) {
        // Comparing A+B+C and B+C+D is the same as comparing A and D directly
        int increased = 0;
        for (int i = 3; i < input.size(); i++) {
            if (input.get(i - 3) < input.get(i)) {
                increased++;
            }
        }
        return increased;
    }

    @InputParser
    public static List<Integer> parse(Stream<String> stream) {
        return stream.map(Integer::parseInt).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day01.txt"));

        List<Integer> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
