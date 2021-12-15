package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day
public class Day15 {
    public record Matrix(int sizeX, int sizeY, List<Integer> data) {
        public boolean isInside(Point p) {
            return p.x < sizeX && p.y < sizeY && p.x >= 0 && p.y >= 0;
        }

        public int getData(Point p) {
            return getData(p.x, p.y);
        }

        public int getData(int x, int y) {
            return data.get(y * sizeX + x);
        }
    }

    public record Point(int x, int y) {
        public List<Point> getAdjacentPoints() {
            return List.of(
                    new Point(x - 1, y),
                    new Point(x, y - 1),
                    new Point(x + 1, y),
                    new Point(x, y + 1)
            );
        }
    }

    public record Pair(Point p, int risk) {

    }

    @Part1
    public static long part1(Matrix input) {
        // Dijkstra
        Map<Point, Integer> risks = new HashMap<>();

        Set<Point> q = new HashSet<>();
        q.add(new Point(0, 0));

        risks.put(new Point(0, 0), 0);

        while (!q.isEmpty()) {
            Pair toExplore = q.stream()
                    .map(p -> new Pair(p, risks.get(p)))
                    .min(Comparator.comparing(Pair::risk))
                    .get();
            Point point = toExplore.p;
            int riskLevel = toExplore.risk;

            q.remove(point);

            for (Point adjacentPoint : point.getAdjacentPoints()) {
                if (!input.isInside(adjacentPoint)) {
                    continue;
                }

                int adjacentPointRisk = riskLevel + input.getData(adjacentPoint);

                risks.compute(adjacentPoint, (p, v) -> {
                    if (v == null || v > adjacentPointRisk) {
                        q.add(p);
                        return adjacentPointRisk;
                    } else {
                        return v;
                    }
                });
            }
        }

        return risks.get(new Point(input.sizeX - 1, input.sizeY - 1));
    }

    @Part2
    public static long part2(Matrix input) {
        Point max5 = new Point(input.sizeX * 5 - 1, input.sizeY * 5 - 1);

        Map<Point, Integer> risks = new HashMap<>();
        risks.put(new Point(0, 0), 0);

        TreeMap<Integer, List<Point>> q = new TreeMap<>();
        q.put(0, List.of(new Point(0, 0)));

        while (!q.isEmpty()) {
            Map.Entry<Integer, List<Point>> e = q.pollFirstEntry();

            int riskLevel = e.getKey();
            for (Point point : e.getValue()) {
                for (Point adjacentPoint : point.getAdjacentPoints()) {
                    if (adjacentPoint.x < 0
                            || adjacentPoint.y < 0
                            || adjacentPoint.x >= input.sizeX * 5
                            || adjacentPoint.y >= input.sizeY * 5) {
                        continue;
                    }

                    int adjacentPointRisk = riskLevel + getValue(input, adjacentPoint);

                    // No need to compute the rest
                    if (adjacentPoint.equals(max5)) {
                        return adjacentPointRisk;
                    }

                    risks.compute(adjacentPoint, (p, v) -> {
                        if (v == null || v > adjacentPointRisk) {
                            // Remove from old cost
                            if (v != null) {
                                q.get(v).remove(p);
                            }
                            // Add to new cost
                            q.compute(adjacentPointRisk, (i, l) -> {
                                if (l == null) {
                                    l = new ArrayList<>();
                                }
                                l.add(p);
                                return l;
                            });
                            return adjacentPointRisk;
                        } else {
                            return v;
                        }
                    });
                }
            }
        }

        return risks.get(max5);
    }

    private static int getValue(Matrix input, Point adjacentPoint) {
        // Old value
        int value = input.getData(adjacentPoint.x % input.sizeX, adjacentPoint.y % input.sizeY);
        // Add one each repetition
        value += (adjacentPoint.x / input.sizeX) + (adjacentPoint.y / input.sizeY);
        // Wrap back to 1
        while (value >= 10) {
            value -= 9;
        }
        return value;
    }

    @InputParser
    public static Matrix parse(Stream<String> stream) {
        List<String> strings = stream.toList();
        int sizeX = strings.get(0).length();
        int sizeY = strings.size();

        List<Integer> content = strings.stream()
                .flatMap(s -> s.chars().mapToObj(i -> i - '0'))
                .collect(Collectors.toList());

        return new Matrix(sizeX, sizeY, content);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day15.txt"));
        Matrix l = parse(br.lines());

        System.out.println(part1(l));
        while (true) {
            System.out.println(part2(l));
        }
    }
}
