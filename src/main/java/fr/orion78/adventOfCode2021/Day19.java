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

    @Part1
    public static long part1(List<Scanner> input) {
        Set<Point> points = new HashSet<>(input.get(0).relativeBeacons);

        List<Scanner> scannersToAssemble = new ArrayList<>(input.subList(1, input.size()));

        while (!scannersToAssemble.isEmpty()) {
            nextScan:
            for (Iterator<Scanner> iterator = scannersToAssemble.iterator(); iterator.hasNext(); ) {
                Scanner scanner = iterator.next();

                for (Orientation orientation : Orientation.values()) {
                    List<Point> rotatedPoints = scanner.relativeBeacons.stream()
                            .map(orientation::rotate)
                            .toList();
                    for (Point rotatedPoint : rotatedPoints) {
                        for (Point point : points) {
                            Point translation = rotatedPoint.getTranslationFor(point);

                            List<Point> rotatedTranslatedPoints = rotatedPoints.stream()
                                    .map(p -> p.translate(translation))
                                    .toList();

                            if (rotatedTranslatedPoints.stream()
                                    .filter(points::contains)
                                    .count() >= 12) {
                                iterator.remove();
                                points.addAll(rotatedTranslatedPoints);
                                continue nextScan;
                            }
                        }
                    }
                }
            }
        }

        return points.size();
    }

    @Part2
    public static long part2(List<Scanner> input) {
        Set<Point> points = new HashSet<>(input.get(0).relativeBeacons);

        List<Scanner> scannersToAssemble = new ArrayList<>(input.subList(1, input.size()));

        List<Point> scannerPos = new ArrayList<>();
        scannerPos.add(new Point(0, 0, 0));

        while (!scannersToAssemble.isEmpty()) {
            nextScan:
            for (Iterator<Scanner> iterator = scannersToAssemble.iterator(); iterator.hasNext(); ) {
                Scanner scanner = iterator.next();

                for (Orientation orientation : Orientation.values()) {
                    List<Point> rotatedPoints = scanner.relativeBeacons.stream()
                            .map(orientation::rotate)
                            .toList();
                    for (Point rotatedPoint : rotatedPoints) {
                        for (Point point : points) {
                            Point translation = rotatedPoint.getTranslationFor(point);

                            List<Point> rotatedTranslatedPoints = rotatedPoints.stream()
                                    .map(p -> p.translate(translation))
                                    .toList();

                            if (rotatedTranslatedPoints.stream()
                                    .filter(points::contains)
                                    .count() >= 12) {
                                iterator.remove();
                                points.addAll(rotatedTranslatedPoints);
                                scannerPos.add(translation);
                                continue nextScan;
                            }
                        }
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

        return maxDistance;
    }

    private static long manhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x)
                + Math.abs(p1.y - p2.y)
                + Math.abs(p1.z - p2.z);
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

        System.out.println(part1(l));
        while (true) {
            System.out.println(part2(l));
        }
    }
}
