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
public class Day05 {
    public record Point(int x, int y) {
        public Point(String x, String y) {
            this(Integer.parseInt(x), Integer.parseInt(y));
        }
    }

    public record Line(Point from, Point to) {
        boolean isVertical() {
            return from.x == to.x;
        }

        boolean isHorizontal() {
            return from.y == to.y;
        }

        boolean isVerticalOrHorizontal() {
            return isVertical() || isHorizontal();
        }

        Stream<Point> points() {
            int iterx = Integer.signum(to.x - from.x);
            int itery = Integer.signum(to.y - from.y);

            /*
            Equivalent to :

            if (from.x == to.x) {
                iterx = 0;
            } else if(from.x < to.x) {
                iterx = 1;
            } else {
                iterx = -1;
            }
            */

            List<Point> pts = new ArrayList<>(Math.max(Math.abs(to.x - from.x), Math.abs(to.y - from.y)));

            int x = from.x;
            int y = from.y;

            while (x != to.x || y != to.y) {
                pts.add(new Point(x, y));
                x += iterx;
                y += itery;
            }
            pts.add(to);

            return pts.stream();
        }
    }

    @Part1
    public static long part1(List<Line> input) {
        Map<Point, Integer> pointsHV = input.stream()
                .filter(Line::isVerticalOrHorizontal)
                .flatMap(Line::points)
                .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

        return pointsHV.entrySet().stream()
                .filter(x -> x.getValue() > 1)
                .count();
    }

    @Part2
    public static long part2(List<Line> input) {
        Map<Point, Integer> points = input.stream()
                .flatMap(Line::points)
                .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

        return points.entrySet().stream()
                .filter(x -> x.getValue() > 1)
                .count();
    }

    @Part1(optLevel = 1)
    public static long part1v2(List<Line> input) {
        Set<Point> intersectPoints = new HashSet<>(10_000);

        for (int i = 0; i < input.size(); i++) {
            Line l = input.get(i);
            if (!l.isVerticalOrHorizontal()) {
                continue;
            }
            for (int j = i + 1; j < input.size(); j++) {
                Line r = input.get(j);
                if (!r.isVerticalOrHorizontal()) {
                    continue;
                }

                Set<Point> intersect = intersect(l, r);
                intersectPoints.addAll(intersect);
            }
        }

        return intersectPoints.size();
    }

    public static Set<Point> intersect(Line l, Line r) {
        if (l.isVertical() && r.isVertical()) {
            // Both vertical, check if on same line then get intersection if any
            if (l.from.x == r.from.x) {
                int from = Math.max(Math.min(l.from.y, l.to.y), Math.min(r.from.y, r.to.y));
                int to = Math.min(Math.max(l.from.y, l.to.y), Math.max(r.from.y, r.to.y));

                if (from > to) {
                    return Collections.emptySet();
                }

                Set<Point> points = new HashSet<>(to - from);
                for (int y = from; y <= to; y++) {
                    points.add(new Point(l.from.x, y));
                }
                return points;
            } else {
                return Collections.emptySet();
            }
        } else if (l.isHorizontal() && r.isHorizontal()) {
            // Both horizontal, check if on same line then get intersection if any
            if (l.from.y == r.from.y) {
                int from = Math.max(Math.min(l.from.x, l.to.x), Math.min(r.from.x, r.to.x));
                int to = Math.min(Math.max(l.from.x, l.to.x), Math.max(r.from.x, r.to.x));

                if (from > to) {
                    return Collections.emptySet();
                }

                Set<Point> points = new HashSet<>(to - from);
                for (int x = from; x <= to; x++) {
                    points.add(new Point(x, l.from.y));
                }
                return points;
            } else {
                return Collections.emptySet();
            }
        } else if ((l.isVertical() && r.isHorizontal())
                || (l.isHorizontal() && r.isVertical())) {
            // One vertical and the other horizontal, may be only one intersection point
            Point intersect = new Point(l.isVertical() ? l.from.x : r.from.x,
                    l.isVertical() ? r.from.y : l.from.y);
            if (l.isVertical()) {
                if (isBetween(intersect.x, r.from.x, r.to.x)
                        && isBetween(intersect.y, l.from.y, l.to.y)) {
                    return Set.of(intersect);
                } else {
                    return Collections.emptySet();
                }
            } else {
                if (isBetween(intersect.x, l.from.x, l.to.x)
                        && isBetween(intersect.y, r.from.y, r.to.y)) {
                    return Set.of(intersect);
                } else {
                    return Collections.emptySet();
                }
            }
        } else if (l.isVertical()) {
            long numerator = (long) r.from.y * (r.to.x - l.from.x) - (long) r.to.y * (r.from.x - l.from.x);
            long denominator = r.to.x - r.from.x;

            if (numerator % denominator == 0) {
                int y = (int) (numerator / denominator);

                if (isBetween(y, r.from.y, r.to.y)
                        && isBetween(y, l.from.y, l.to.y)) {
                    return Set.of(new Point(l.from.x, y));
                }
            }

            return Collections.emptySet();
        } else if (l.isHorizontal()) {
            long numerator = (long) r.from.x * (r.to.y - l.from.y) - (long) r.to.x * (r.from.y - l.from.y);
            long denominator = r.to.y - r.from.y;

            if (numerator % denominator == 0) {
                int x = (int) (numerator / denominator);

                if (isBetween(x, r.from.x, r.to.x)
                        && isBetween(x, l.from.x, l.to.x)) {
                    return Set.of(new Point(x, l.from.y));
                }
            }

            return Collections.emptySet();
        } else if (r.isVertical()) {
            long numerator = (long) l.from.y * (l.to.x - r.from.x) - (long) l.to.y * (l.from.x - r.from.x);
            long denominator = l.to.x - l.from.x;

            if (numerator % denominator == 0) {
                int y = (int) (numerator / denominator);

                if (isBetween(y, r.from.y, r.to.y)
                        && isBetween(y, l.from.y, l.to.y)) {
                    return Set.of(new Point(r.from.x, y));
                }
            }

            return Collections.emptySet();
        } else if (r.isHorizontal()) {
            long numerator = (long) l.from.x * (l.to.y - r.from.y) - (long) l.to.x * (l.from.y - r.from.y);
            long denominator = l.to.y - l.from.y;

            if (numerator % denominator == 0) {
                int x = (int) (numerator / denominator);

                if (isBetween(x, r.from.x, r.to.x)
                        && isBetween(x, l.from.x, l.to.x)) {
                    return Set.of(new Point(x, r.from.y));
                }
            }

            return Collections.emptySet();
        } else {
            // Both diagonal


            return Collections.emptySet();
        }
    }

    public static boolean isBetween(int value, int a, int b) {
        return value >= Math.min(a, b) && value <= Math.max(a, b);
    }

    @Part2(optLevel = 1)
    public static long part2v2(List<Line> input) {
        Set<Point> intersectPoints = new HashSet<>(30_000);

        for (int i = 0; i < input.size(); i++) {
            Line l = input.get(i);
            for (int j = i + 1; j < input.size(); j++) {
                Line r = input.get(j);

                Set<Point> intersect = intersect(l, r);
                if (intersect != null) {
                    intersectPoints.addAll(intersect);
                }
            }
        }

        return intersectPoints.size();
    }

    @InputParser
    public static List<Line> parse(Stream<String> stream) {
        List<String> l = stream.toList();

        List<Line> lines = new ArrayList<>(l.size());

        for (String s : l) {
            String[] split = s.split(" -> ");
            String[] from = split[0].split(",");
            String[] to = split[1].split(",");
            lines.add(new Line(new Point(from[0], from[1]), new Point(to[0], to[1])));
        }

        return lines;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day05.txt"));
        List<Line> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part1v2(l));
        System.out.println(part2(l));
        System.out.println(part2v2(l));
    }
}
