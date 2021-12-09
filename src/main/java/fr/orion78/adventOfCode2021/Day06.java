package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day06 {
    @Part1
    public static long part1(Map<Integer, Long> m) {
        for (int curDay = 0; curDay < 80; curDay++) {
            long curFish = m.getOrDefault(curDay, 0L);
            m.put(curDay + 7, m.getOrDefault(curDay + 7, 0L) + curFish);
            m.put(curDay + 9, m.getOrDefault(curDay + 9, 0L) + curFish);
        }

        return m.entrySet().stream()
                .filter(e -> e.getKey() >= 80)
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    @Part2
    public static long part2(Map<Integer, Long> m) {
        for (int curDay = 0; curDay < 256; curDay++) {
            long curFish = m.getOrDefault(curDay, 0L);
            m.put(curDay + 7, m.getOrDefault(curDay + 7, 0L) + curFish);
            m.put(curDay + 9, m.getOrDefault(curDay + 9, 0L) + curFish);
        }

        return m.entrySet().stream()
                .filter(e -> e.getKey() >= 256)
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    @InputParser
    public static Map<Integer, Long> parse(Stream<String> stream) {
        return stream.flatMap(x -> Arrays.stream(x.split(",")))
                .collect(Collectors.toMap(Integer::parseInt, x -> 1L, Long::sum));
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day06.txt"));
        Map<Integer, Long> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
