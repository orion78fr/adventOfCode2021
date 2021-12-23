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
public class Day22 {
    public record Result(long part1, long part2) {

    }

    public record Step(boolean lit, long xMin, long xMax, long yMin, long yMax, long zMin, long zMax) {

    }

    public record Point(long x, long y, long z) {

    }

    public static abstract class Node {
        long xMin;
        long xMax;
        long yMin;
        long yMax;
        long zMin;
        long zMax;

        public Node(long xMin, long xMax, long yMin, long yMax, long zMin, long zMax) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;
        }

        public boolean intersects(Node n) {
            return xMin < n.xMax && xMax > n.xMin
                    && yMin < n.yMax && yMax > n.yMin
                    && zMin < n.zMax && zMax > n.zMin;
        }

        public abstract Node step(Step step);

        public abstract long countLit();
    }

    public static class Tree extends Node {
        List<Node> children;

        public Tree(long xMin, long xMax, long yMin, long yMax, long zMin, long zMax, List<Node> children) {
            super(xMin, xMax, yMin, yMax, zMin, zMax);
            this.children = children;
        }

        public long countLit() {
            return children.stream().mapToLong(Node::countLit).sum();
        }

        public Node step(Step step) {
            if (!intersects(new Leaf(step))) {
                return this;
            } else {
                return new Tree(xMin, xMax, yMin, yMax, zMin, zMax,
                        children.stream()
                                .map(n -> n.step(step))
                                .toList());
            }
        }
    }

    public static class Leaf extends Node {
        boolean lit;

        public Leaf(long xMin, long xMax, long yMin, long yMax, long zMin, long zMax, boolean lit) {
            super(xMin, xMax, yMin, yMax, zMin, zMax);
            this.lit = lit;
        }

        public Leaf(Step step) {
            super(step.xMin, step.xMax, step.yMin, step.yMax, step.zMin, step.zMax);
        }

        public long countLit() {
            if (lit) {
                return (xMax - xMin) * (yMax - yMin) * (zMax - zMin);
            } else {
                return 0;
            }
        }

        public Node step(Step step) {
            // No change to lighting
            if (step.lit == lit) {
                return this;
            }

            // No intersection
            if (!intersects(new Leaf(step))) {
                return this;
            }

            // Fully inside
            if (xMin >= step.xMin && xMin < step.xMax &&
                    xMax > step.xMin && xMax <= step.xMax &&
                    yMin >= step.yMin && yMin < step.yMax &&
                    yMax > step.yMin && yMax <= step.yMax &&
                    zMin >= step.zMin && zMin < step.zMax &&
                    zMax > step.zMin && zMax <= step.zMax) {
                return new Leaf(xMin, xMax, yMin, yMax, zMin, zMax, step.lit);
            }

            TreeSet<Long> xCoords = new TreeSet<>();
            xCoords.add(xMin);
            xCoords.add(xMax);
            if (step.xMin > xMin && step.xMin < xMax) {
                xCoords.add(step.xMin);
            }
            if (step.xMax > xMin && step.xMax < xMax) {
                xCoords.add(step.xMax);
            }

            TreeSet<Long> yCoords = new TreeSet<>();
            yCoords.add(yMin);
            yCoords.add(yMax);
            if (step.yMin > yMin && step.yMin < yMax) {
                yCoords.add(step.yMin);
            }
            if (step.yMax > yMin && step.yMax < yMax) {
                yCoords.add(step.yMax);
            }

            TreeSet<Long> zCoords = new TreeSet<>();
            zCoords.add(zMin);
            zCoords.add(zMax);
            if (step.zMin > zMin && step.zMin < zMax) {
                zCoords.add(step.zMin);
            }
            if (step.zMax > zMin && step.zMax < zMax) {
                zCoords.add(step.zMax);
            }

            List<Node> children = new ArrayList<>();

            Iterator<Long> x = xCoords.iterator();
            long lowX;
            long highX = x.next();

            while (x.hasNext()) {
                lowX = highX;
                highX = x.next();

                Iterator<Long> y = yCoords.iterator();
                long lowY;
                long highY = y.next();

                while (y.hasNext()) {
                    lowY = highY;
                    highY = y.next();

                    Iterator<Long> z = zCoords.iterator();
                    long lowZ;
                    long highZ = z.next();

                    while (z.hasNext()) {
                        lowZ = highZ;
                        highZ = z.next();

                        Leaf l = new Leaf(lowX, highX, lowY, highY, lowZ, highZ, false);
                        l.lit = l.intersects(new Leaf(step)) ? step.lit : !step.lit;

                        children.add(l);
                    }
                }
            }

            return new Tree(xMin, xMax, yMin, yMax, zMin, zMax, children);
        }
    }

    @Part1
    public static long part1(List<Step> input) {
        Set<Point> lit = new HashSet<>();

        for (Step step : input) {
            for (long x = Math.max(-50, step.xMin); x < Math.min(51, step.xMax); x++) {
                for (long y = Math.max(-50, step.yMin); y < Math.min(51, step.yMax); y++) {
                    for (long z = Math.max(-50, step.zMin); z < Math.min(51, step.zMax); z++) {
                        if (step.lit) {
                            lit.add(new Point(x, y, z));
                        } else {
                            lit.remove(new Point(x, y, z));
                        }
                    }
                }
            }
        }

        return lit.size();
    }

    @Part1(optLevel = 1, bothParts = true)
    public static Result bothParts(List<Step> input) {
        Node tree = new Leaf(Integer.MIN_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MAX_VALUE, false);

        for (Step step : input) {
            tree = tree.step(step);
        }

        Node tree50 = tree
                .step(new Step(false, 51, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE))
                .step(new Step(false, Integer.MIN_VALUE, -50,
                        Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE))
                .step(new Step(false, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        51, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE))
                .step(new Step(false, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, -50,
                        Integer.MIN_VALUE, Integer.MAX_VALUE))
                .step(new Step(false, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE,
                        51, Integer.MAX_VALUE))
                .step(new Step(false, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MIN_VALUE, -50));

        return new Result(tree50.countLit(), tree.countLit());
    }

    @Part2(optLevel = -1)
    public static long part2v2(List<Step> input) {
        Set<Long> xValues = new TreeSet<>();
        Set<Long> yValues = new TreeSet<>();
        Set<Long> zValues = new TreeSet<>();

        xValues.add(Long.MIN_VALUE);
        xValues.add(Long.MAX_VALUE);
        yValues.add(Long.MIN_VALUE);
        yValues.add(Long.MAX_VALUE);
        zValues.add(Long.MIN_VALUE);
        zValues.add(Long.MAX_VALUE);

        for (Step step : input) {
            xValues.add(step.xMin);
            xValues.add(step.xMax);
            yValues.add(step.yMin);
            yValues.add(step.yMax);
            zValues.add(step.zMin);
            zValues.add(step.zMax);
        }

        long count = 0;

        Iterator<Long> x = xValues.iterator();
        long lowX;
        long highX = x.next();

        while (x.hasNext()) {
            lowX = highX;
            highX = x.next();

            Iterator<Long> y = yValues.iterator();
            long lowY;
            long highY = y.next();

            while (y.hasNext()) {
                lowY = highY;
                highY = y.next();

                Iterator<Long> z = zValues.iterator();
                long lowZ;
                long highZ = z.next();

                nextZ:
                while (z.hasNext()) {
                    lowZ = highZ;
                    highZ = z.next();

                    Leaf l = new Leaf(lowX, highX, lowY, highY, lowZ, highZ, true);

                    for (int i = input.size() - 1; i >= 0; i--) {
                        Step step = input.get(i);

                        if (l.intersects(new Leaf(step))) {
                            if (step.lit) {
                                count += l.countLit();
                            }
                            continue nextZ;
                        }
                    }
                }
            }
        }

        return count;
    }

    @InputParser
    public static List<Step> parse(Stream<String> stream) {
        return stream.map(s -> {
            String[] coords = s.split(" ")[1].split(",");
            String[] x = coords[0].substring(2).split("\\.\\.");
            String[] y = coords[1].substring(2).split("\\.\\.");
            String[] z = coords[2].substring(2).split("\\.\\.");

            return new Step(s.startsWith("on"), Integer.parseInt(x[0]), Integer.parseInt(x[1]) + 1,
                    Integer.parseInt(y[0]), Integer.parseInt(y[1]) + 1,
                    Integer.parseInt(z[0]), Integer.parseInt(z[1]) + 1);
        }).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day22.txt"));
        List<Step> l = parse(br.lines());

        System.out.println(bothParts(l));
    }
}
