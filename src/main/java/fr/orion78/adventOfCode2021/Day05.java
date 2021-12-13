package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        boolean isVerticalOrHorizontal() {
            return from.x == to.x || from.y == to.y;
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

        while (true) {
            System.out.println(part1(l));
            System.out.println(part2(l));
        }
    }
}
