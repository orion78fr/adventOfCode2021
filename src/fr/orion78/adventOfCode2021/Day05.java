package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

            List<Point> pts = new ArrayList<>();

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

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day05.txt"));
        List<String> l = br.lines().toList();

        List<Line> lines = new ArrayList<>(l.size());

        for (String s : l) {
            String[] split = s.split(" -> ");
            String[] from = split[0].split(",");
            String[] to = split[1].split(",");
            lines.add(new Line(new Point(from[0], from[1]), new Point(to[0], to[1])));
        }

        Map<Point, Integer> pointsHV = lines.stream()
                .filter(Line::isVerticalOrHorizontal)
                .flatMap(Line::points)
                .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

        long intersectionsHV = pointsHV.entrySet().stream()
                .filter(x -> x.getValue() > 1)
                .count();

        System.out.println(intersectionsHV);

        Map<Point, Integer> points = lines.stream()
                .flatMap(Line::points)
                .collect(Collectors.toMap(Function.identity(), x -> 1, Integer::sum));

        long intersections = points.entrySet().stream()
                .filter(x -> x.getValue() > 1)
                .count();

        System.out.println(intersections);
    }
}
