package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Day09 {
    public record Point(int x, int y) {
    }

    @Part1
    public static long part1(List<String> input) {
        long totalRisk = 0;

        for (int i = 0; i < input.size(); i++) {
            String l = input.get(i);
            for (int j = 0; j < l.length(); j++) {
                int c = l.codePointAt(j);
                if (
                        (i + 1 < input.size() && input.get(i + 1).codePointAt(j) <= c) ||
                                (i - 1 >= 0 && input.get(i - 1).codePointAt(j) <= c) ||
                                (j + 1 < l.length() && input.get(i).codePointAt(j + 1) <= c) ||
                                (j - 1 >= 0 && input.get(i).codePointAt(j - 1) <= c)
                ) {
                    continue;
                }
                totalRisk += c - '0' + 1;
            }
        }

        return totalRisk;
    }

    @Part2
    public static long part2(List<String> input) {
        Set<Point> exploredPoints = new HashSet<>();
        List<Integer> basinSizes = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            String l = input.get(i);
            for (int j = 0; j < l.length(); j++) {
                int c = l.codePointAt(j);
                if (c == '9') {
                    continue;
                }
                if (exploredPoints.contains(new Point(i, j))) {
                    continue;
                }

                int basinSize = 0;
                Queue<Point> toExplore = new ArrayDeque<>();
                toExplore.add(new Point(i, j));

                while (!toExplore.isEmpty()) {
                    Point p = toExplore.remove();
                    if (!exploredPoints.add(p)) {
                        continue;
                    }

                    if (input.get(p.x).codePointAt(p.y) == '9') {
                        continue;
                    }

                    basinSize++;

                    if (p.x + 1 < input.size()) {
                        toExplore.add(new Point(p.x + 1, p.y));
                    }
                    if (p.x - 1 >= 0) {
                        toExplore.add(new Point(p.x - 1, p.y));
                    }
                    if (p.y + 1 < l.length()) {
                        toExplore.add(new Point(p.x, p.y + 1));
                    }
                    if (p.y - 1 >= 0) {
                        toExplore.add(new Point(p.x, p.y - 1));
                    }
                }

                basinSizes.add(basinSize);
            }
        }

        List<Integer> biggest = basinSizes.stream().sorted(Comparator.reverseOrder()).limit(3).toList();

        return biggest.get(0) * biggest.get(1) * biggest.get(2);
    }

    @InputParser
    public static List<String> parse(Stream<String> stream) {
        return stream.toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day09.txt"));
        List<String> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
