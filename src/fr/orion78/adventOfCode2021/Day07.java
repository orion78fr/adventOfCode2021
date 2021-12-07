package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day07 {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day07.txt"));
        List<Integer> l = br.lines()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .map(Integer::parseInt)
                .sorted()
                .toList();

        int min = IntStream.range(l.get(0), l.get(l.size() - 1) + 1)
                .map(i -> l.stream()
                        .mapToInt(x -> Math.abs(x - i))
                        .sum())
                .min().getAsInt();

        System.out.println(min);

        min = IntStream.range(l.get(0), l.get(l.size() - 1) + 1)
                .map(i -> l.stream()
                        .mapToInt(x -> {
                            int dist = Math.abs(x - i);
                            return dist * (dist + 1) / 2;
                        })
                        .sum())
                .min().getAsInt();

        System.out.println(min);

        // Works for part 1
        int median = (l.get(l.size() / 2 - 1) + l.get(l.size() / 2)) / 2;
        min = l.stream()
                .mapToInt(x -> Math.abs(x - median))
                .sum();

        System.out.println(min);

        List<Integer> integers = IntStream.range(l.get(0), l.get(l.size() - 1) + 1)
                .mapToObj(i -> l.stream()
                        .mapToInt(x -> {
                            int dist = Math.abs(x - i);
                            return dist * (dist + 1) / 2;
                        })
                        .sum())
                .toList();
        System.out.println();
    }
}
