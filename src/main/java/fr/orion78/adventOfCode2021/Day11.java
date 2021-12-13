package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

@Day
public class Day11 {
    public record Point(int x, int y) {
        public List<Point> getAdjacentPoints() {
            return List.of(
                    new Point(x - 1, y),
                    new Point(x - 1, y - 1),
                    new Point(x, y - 1),
                    new Point(x + 1, y - 1),
                    new Point(x + 1, y),
                    new Point(x + 1, y + 1),
                    new Point(x, y + 1),
                    new Point(x - 1, y + 1)
            );
        }
    }

    @Part1
    public static long part1(List<List<Integer>> input) {
        long flashes = 0;

        Map<Point, Integer> m = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            List<Integer> l = input.get(i);
            for (int j = 0; j < l.size(); j++) {
                m.put(new Point(j, i), l.get(j));
            }
        }

        for (int i = 0; i < 100; i++) {
            Set<Point> flashing = new HashSet<>();
            int flashingBefore;

            // Increase all by one
            for (Point p : m.keySet()) {
                m.compute(p, (x, y) -> y + 1);
            }

            do {
                flashingBefore = flashing.size();

                for (Point p : m.keySet()) {
                    if (!flashing.contains(p) && m.get(p) > 9) {
                        // It should flash
                        p.getAdjacentPoints().forEach(
                                x -> m.computeIfPresent(x, (y, n) -> n + 1)
                        );

                        flashing.add(p);
                    }
                }
            } while (flashingBefore < flashing.size());

            // Reset all that flashed to 0
            for (Point p : flashing) {
                m.put(p, 0);
            }

            flashes += flashing.size();
        }

        return flashes;
    }

    @Part2
    public static long part2(List<List<Integer>> input) {
        Map<Point, Integer> m = new HashMap<>();

        for (int i = 0; i < input.size(); i++) {
            List<Integer> l = input.get(i);
            for (int j = 0; j < l.size(); j++) {
                m.put(new Point(j, i), l.get(j));
            }
        }

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Set<Point> flashing = new HashSet<>();
            int flashingBefore;

            // Increase all by one
            for (Point p : m.keySet()) {
                m.compute(p, (x, y) -> y + 1);
            }

            do {
                flashingBefore = flashing.size();

                for (Point p : m.keySet()) {
                    if (!flashing.contains(p) && m.get(p) > 9) {
                        // It should flash
                        p.getAdjacentPoints().forEach(
                                x -> m.computeIfPresent(x, (y, n) -> n + 1)
                        );

                        flashing.add(p);
                    }
                }
            } while (flashingBefore < flashing.size());

            // All flashed synchronously
            if (flashing.size() == m.size()) {
                return i + 1;
            }

            // Reset all that flashed to 0
            for (Point p : flashing) {
                m.put(p, 0);
            }
        }

        return -1;
    }

    @InputParser
    public static List<List<Integer>> parse(Stream<String> stream) {
        return stream.map(s -> s.chars().map(i -> i - '0').boxed().toList()).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day11.txt"));
        List<List<Integer>> l = parse(br.lines());
        /*List<List<Integer>> l = parse(
                Stream.of("11111",
                        "19991",
                        "19191",
                        "19991",
                        "11111")
        );*/
        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
