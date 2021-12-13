package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day13 {
    public record Input(List<Point> points, List<Fold> folds) {
    }

    public record Point(int x, int y) {
        public Point fold(Fold fold) {
            if (fold.type == FoldType.HORIZONTAL) {
                if (y <= fold.x) {
                    return this;
                } else {
                    return new Point(x, fold.x - (y - fold.x));
                }
            } else {
                if (x <= fold.x) {
                    return this;
                } else {
                    return new Point(fold.x - (x - fold.x), y);
                }
            }
        }
    }

    public record Fold(FoldType type, int x) {
    }

    public enum FoldType {
        HORIZONTAL, VERTICAL
    }

    @Part1
    public static long part1(Input input) {
        Set<Point> afterFold = new HashSet<>();

        Fold f = input.folds.get(0);
        for (Point point : input.points) {
            afterFold.add(point.fold(f));
        }

        return afterFold.size();
    }

    @Part2
    public static String part2(Input input) {
        Set<Point> points = new HashSet<>(input.points);
        for (Fold f : input.folds) {
            Set<Point> afterFold = new HashSet<>(points.size());

            for (Point point : points) {
                afterFold.add(point.fold(f));
            }

            points = afterFold;
        }

        int maxX = points.stream().mapToInt(Point::x).max().getAsInt();
        int maxY = points.stream().mapToInt(Point::y).max().getAsInt();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                if (points.contains(new Point(j, i))) {
                    sb.append('X');
                } else {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    @InputParser
    public static Input parse(Stream<String> stream) {
        List<String> strings = stream.toList();
        List<Point> points = new ArrayList<>();
        List<Fold> folds = new ArrayList<>();

        for (String str : strings) {
            if (str.isEmpty()) {
                continue;
            }
            if (str.startsWith("fold along")) {
                String[] splits = str.split("=");

                FoldType foldType;
                if (splits[0].charAt(splits[0].length() - 1) == 'x') {
                    foldType = FoldType.VERTICAL;
                } else {
                    foldType = FoldType.HORIZONTAL;
                }

                folds.add(new Fold(foldType, Integer.parseInt(splits[1])));
            } else {
                String[] splits = str.split(",");
                points.add(new Point(Integer.parseInt(splits[0]), Integer.parseInt(splits[1])));
            }
        }

        return new Input(points, folds);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day13.txt"));
        Input l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
