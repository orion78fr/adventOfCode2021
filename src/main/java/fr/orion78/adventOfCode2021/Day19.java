package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

@Day
public class Day19 {
    public record Point(int x, int y, int z) {
        public Point getTranslationFor(Point p) {
            return new Point(p.x - x, p.y - y, p.z - z);
        }

        public Point translate(Point p) {
            return new Point(x + p.x, y + p.y, z + p.z);
        }
    }

    public record Scanner(int id, List<Point> relativeBeacons) {

    }

    public record Result(long part1, long part2) {

    }

    public record Pair(Point p, List<Point> trp) {

    }

    public enum Orientation {
        Rot0(1, 0, 0,
                0, 1, 0,
                0, 0, 1),
        Rot1(0, -1, 0,
                1, 0, 0,
                0, 0, 1),
        Rot2(-1, 0, 0,
                0, -1, 0,
                0, 0, 1),
        Rot3(0, 1, 0,
                -1, 0, 0,
                0, 0, 1),

        Rot4(0, 0, 1,
                1, 0, 0,
                0, 1, 0),
        Rot5(-1, 0, 0,
                0, 0, 1,
                0, 1, 0),
        Rot6(0, 0, -1,
                -1, 0, 0,
                0, 1, 0),
        Rot7(1, 0, 0,
                0, 0, -1,
                0, 1, 0),

        Rot8(0, 1, 0,
                0, 0, 1,
                1, 0, 0),
        Rot9(0, 0, -1,
                0, 1, 0,
                1, 0, 0),
        Rot10(0, -1, 0,
                0, 0, -1,
                1, 0, 0),
        Rot11(0, 0, 1,
                0, -1, 0,
                1, 0, 0),

        Rot12(0, 1, 0,
                1, 0, 0,
                0, 0, -1),
        Rot13(-1, 0, 0,
                0, 1, 0,
                0, 0, -1),
        Rot14(0, -1, 0,
                -1, 0, 0,
                0, 0, -1),
        Rot15(1, 0, 0,
                0, -1, 0,
                0, 0, -1),

        Rot16(0, 0, 1,
                0, 1, 0,
                -1, 0, 0),
        Rot17(0, -1, 0,
                0, 0, 1,
                -1, 0, 0),
        Rot18(0, 0, -1,
                0, -1, 0,
                -1, 0, 0),
        Rot19(0, 1, 0,
                0, 0, -1,
                -1, 0, 0),

        Rot20(1, 0, 0,
                0, 0, 1,
                0, -1, 0),
        Rot21(0, 0, -1,
                1, 0, 0,
                0, -1, 0),
        Rot22(-1, 0, 0,
                0, 0, -1,
                0, -1, 0),
        Rot23(0, 0, 1,
                -1, 0, 0,
                0, -1, 0),
        ;

        public final int[] values;

        Orientation(int... values) {
            this.values = values;
        }

        public Point rotate(Point p) {
            return new Point(p.x * values[0] + p.y * values[1] + p.z * values[2],
                    p.x * values[3] + p.y * values[4] + p.z * values[5],
                    p.x * values[6] + p.y * values[7] + p.z * values[8]);
        }
    }

    @Part1(bothParts = true)
    public static Result bothParts(List<Scanner> input) {
        Set<Point> points = new HashSet<>(input.get(0).relativeBeacons);

        List<Scanner> scannersToAssemble = new ArrayList<>(input.subList(1, input.size()));

        List<Point> scannerPos = new ArrayList<>();
        scannerPos.add(new Point(0, 0, 0));

        while (!scannersToAssemble.isEmpty()) {
            nextScan:
            for (Iterator<Scanner> iterator = scannersToAssemble.iterator(); iterator.hasNext(); ) {
                Scanner scanner = iterator.next();

                for (Orientation orientation : Orientation.values()) {
                    List<Point> rotatedPoints = rotatePoints(scanner.relativeBeacons, orientation);

                    Optional<Pair> p = cartesianProduct(rotatedPoints, points)
                            .parallel()
                            .map(l -> {
                                Point translation = l.get(0).getTranslationFor(l.get(1));
                                return new Pair(translation, translatePoints(rotatedPoints, translation));
                            })
                            .filter(x -> {
                                int count = 0;
                                for (Point point : x.trp) {
                                    if (points.contains(point)) {
                                        count++;
                                    }
                                    if (count >= 12) {
                                        return true;
                                    }
                                }
                                return false;
                            })
                            .findAny();

                    if (p.isPresent()) {
                        iterator.remove();
                        scannerPos.add(p.get().p);
                        points.addAll(p.get().trp);
                    }
                }
            }
        }

        long maxDistance = Long.MIN_VALUE;

        for (Point p1 : scannerPos) {
            for (Point p2 : scannerPos) {
                maxDistance = Math.max(maxDistance, manhattanDistance(p1, p2));
            }
        }

        return new Result(points.size(), maxDistance);
    }

    private static List<Point> translatePoints(List<Point> rotatedPoints, Point translation) {
        return rotatedPoints.stream()
                .map(p -> p.translate(translation))
                .toList();
    }

    private static List<Point> rotatePoints(List<Point> points, Orientation orientation) {
        return points.stream()
                .map(orientation::rotate)
                .toList();
    }

    private static long manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x)
                + Math.abs(p1.y - p2.y)
                + Math.abs(p1.z - p2.z);
    }

    private static <T> Stream<List<T>> cartesianProduct(Collection<T>... lists) {
        if (lists.length == 0) {
            return Stream.empty();
        }

        Stream<List<T>> stream = lists[0].stream().map(List::of);

        for (int i = 1; i < lists.length; i++) {
            Collection<T> l = lists[i];

            stream = stream.flatMap(x -> l.stream().map(y -> {
                List<T> r = new ArrayList<>(x.size() + 1);
                r.addAll(x);
                r.add(y);
                return r;
            }));
        }

        return stream;
    }

    @InputParser
    public static List<Scanner> parse(Stream<String> stream) {
        List<String> strings = stream.toList();

        List<Scanner> scanners = new ArrayList<>();
        Scanner scanner = null;

        for (String s : strings) {
            if (s.isEmpty()) {
                scanners.add(scanner);
                scanner = null;
            } else if (s.startsWith("---")) {
                scanner = new Scanner(
                        Integer.parseInt(s.substring("--- scanner ".length(), s.length() - " ---".length())),
                        new ArrayList<>());
            } else {
                String[] splits = s.split(",");
                Point p = new Point(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2]));
                scanner.relativeBeacons.add(p);
            }
        }

        if (scanner != null) {
            scanners.add(scanner);
        }

        return scanners;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day19.txt"));
        List<Scanner> l = parse(br.lines());

        while (true) {
            System.out.println(bothParts(l));
        }
    }
}
