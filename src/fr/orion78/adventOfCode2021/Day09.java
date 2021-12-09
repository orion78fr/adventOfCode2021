package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Day09 {
    public record Point(int x, int y) {
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day09.txt"));
        List<String> lines = br.lines().toList();

        //List<String> lines = List.of("2199943210", "3987894921", "9856789892", "8767896789", "9899965678");

        long totalRisk = 0;

        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            for (int j = 0; j < l.length(); j++) {
                int c = l.codePointAt(j);
                if (
                        (i + 1 < lines.size() && lines.get(i + 1).codePointAt(j) <= c) ||
                                (i - 1 >= 0 && lines.get(i - 1).codePointAt(j) <= c) ||
                                (j + 1 < l.length() && lines.get(i).codePointAt(j + 1) <= c) ||
                                (j - 1 >= 0 && lines.get(i).codePointAt(j - 1) <= c)
                ) {
                    continue;
                }
                totalRisk += c - '0' + 1;
            }
        }

        System.out.println(totalRisk);

        Set<Point> exploredPoints = new HashSet<>();
        List<Integer> basinSizes = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
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

                    if (lines.get(p.x).codePointAt(p.y) == '9') {
                        continue;
                    }

                    basinSize++;

                    if (p.x + 1 < lines.size()) {
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

        System.out.println(biggest.get(0) * biggest.get(1) * biggest.get(2));
    }
}
