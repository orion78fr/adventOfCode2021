package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day
public class Day14 {
    public record Input(String init, Map<Pair, Character> transforms) {
        public Character getTransform(char l, char r) {
            return transforms.get(new Pair(l, r));
        }
    }

    public record Pair(char l, char r) {

    }

    @Part1
    public static long part1(Input input) {
        List<Character> l = new ArrayList<>();
        for (int i = 0; i < input.init.length(); i++) {
            l.add(input.init.charAt(i));
        }

        for (int step = 0; step < 10; step++) {
            for (int i = 0; i + 1 < l.size(); i += 2) {
                l.add(i + 1, input.getTransform(l.get(i), l.get(i + 1)));
            }
        }

        Map<Character, Long> m = l.stream().collect(Collectors.toMap(Function.identity(), x -> 1L, Long::sum));

        return m.values().stream().max(Comparator.naturalOrder()).get() - m.values().stream().min(Comparator.naturalOrder()).get();
    }

    @Part1(optLevel = 1)
    public static long part1v2(Input input) {
        Map<Pair, Long> m = new HashMap<>();

        for (int i = 0; i + 1 < input.init.length(); i++) {
            m.compute(new Pair(input.init.charAt(i), input.init.charAt(i + 1)), (k, v) -> v == null ? 1 : v + 1);
        }

        for (int step = 0; step < 10; step++) {
            Map<Pair, Long> m2 = new HashMap<>();
            for (Map.Entry<Pair, Long> e : m.entrySet()) {
                Pair p = e.getKey();
                char c = input.transforms.get(p);

                m2.compute(new Pair(p.l, c), (k, v) -> v == null ? e.getValue() : v + e.getValue());
                m2.compute(new Pair(c, p.r), (k, v) -> v == null ? e.getValue() : v + e.getValue());
            }
            m = m2;
        }

        Map<Character, Long> res = new HashMap<>();

        for (Map.Entry<Pair, Long> e : m.entrySet()) {
            Pair p = e.getKey();

            res.compute(p.l, (k, v) -> v == null ? e.getValue() : v + e.getValue());
            // Don't add the right char, or it will be counted twice
        }

        // Don't forget the rightmost char, which doesn't change
        res.compute(input.init.charAt(input.init.length() - 1), (k, v) -> v == null ? 1 : v + 1);

        return res.values().stream().max(Comparator.naturalOrder()).get() - res.values().stream().min(Comparator.naturalOrder()).get();
    }

    // part2v1 and part2v2 did not finish
    // were recursive version of part1

    @Part2(optLevel = 2)
    public static long part2v3(Input input) {
        Map<Pair, Long> m = new HashMap<>();

        for (int i = 0; i + 1 < input.init.length(); i++) {
            m.compute(new Pair(input.init.charAt(i), input.init.charAt(i + 1)), (k, v) -> v == null ? 1 : v + 1);
        }

        for (int step = 0; step < 40; step++) {
            Map<Pair, Long> m2 = new HashMap<>();
            for (Map.Entry<Pair, Long> e : m.entrySet()) {
                Pair p = e.getKey();
                char c = input.transforms.get(p);

                m2.compute(new Pair(p.l, c), (k, v) -> v == null ? e.getValue() : v + e.getValue());
                m2.compute(new Pair(c, p.r), (k, v) -> v == null ? e.getValue() : v + e.getValue());
            }
            m = m2;
        }

        Map<Character, Long> res = new HashMap<>();

        for (Map.Entry<Pair, Long> e : m.entrySet()) {
            Pair p = e.getKey();

            res.compute(p.l, (k, v) -> v == null ? e.getValue() : v + e.getValue());
            // Don't add the right char, or it will be counted twice
        }

        // Don't forget the rightmost char, which doesn't change
        res.compute(input.init.charAt(input.init.length() - 1), (k, v) -> v == null ? 1 : v + 1);

        return res.values().stream().max(Comparator.naturalOrder()).get() - res.values().stream().min(Comparator.naturalOrder()).get();
    }

    @InputParser
    public static Input parse(Stream<String> stream) {
        List<String> strings = stream.toList();

        Map<Pair, Character> transforms = new HashMap<>(strings.size() - 2);

        for (int i = 2; i < strings.size(); i++) {
            String str = strings.get(i);
            String[] split = str.split(" -> ");

            char l = split[0].charAt(0);
            char r = split[0].charAt(1);
            char c = split[1].charAt(0);
            transforms.put(new Pair(l, r), c);
        }

        return new Input(strings.get(0), transforms);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day14.txt"));
        Input l = parse(br.lines());

        System.out.println(part1v2(l));
        System.out.println(part2v3(l));
    }
}
