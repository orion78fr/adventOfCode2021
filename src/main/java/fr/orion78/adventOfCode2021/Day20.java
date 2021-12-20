package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Day
public class Day20 {
    public record Point(int x, int y) {
        public Stream<Point> neighbors() {
            return Stream.of(
                    new Point(x - 1, y - 1),
                    new Point(x, y - 1),
                    new Point(x + 1, y - 1),
                    new Point(x - 1, y),
                    this,
                    new Point(x + 1, y),
                    new Point(x - 1, y + 1),
                    new Point(x, y + 1),
                    new Point(x + 1, y + 1)
            );
        }
    }

    public record Input(List<Boolean> enhancement, List<Point> litPixels) {

    }

    @Part1
    public static long part1(Input input) {
        Set<Point> litPixels = new HashSet<>(input.litPixels);

        litPixels = doubleStep(input.enhancement, litPixels);

        return litPixels.size();
    }

    public static Set<Point> step(List<Boolean> enhancement, Set<Point> litPixels) {
        return litPixels.stream()
                .flatMap(Point::neighbors) // Consider all points near lit pixels
                .distinct()
                .filter(p -> {
                    int idx = p.neighbors().mapToInt(x -> litPixels.contains(x) ? 1 : 0).reduce(0, (a,b) -> (a << 1) + b);
                    return enhancement.get(idx);
                })
                .collect(Collectors.toSet());
    }

    public static Set<Point> doubleStep(List<Boolean> enhancement, Set<Point> litPixels) {
        if (enhancement.get(0) && !enhancement.get(enhancement.size() - 1)) {
            // Alternating on / off
            Set<Point> unlitPixels = litPixels.stream()
                    .flatMap(Point::neighbors) // Consider all points near lit pixels
                    .distinct()
                    .filter(p -> {
                        int idx = p.neighbors().mapToInt(x -> litPixels.contains(x) ? 1 : 0).reduce(0, (a,b) -> (a << 1) + b);
                        return !enhancement.get(idx);
                    })
                    .collect(Collectors.toSet());

            return unlitPixels.stream()
                    .flatMap(Point::neighbors) // Consider all points near unlit pixels
                    .distinct()
                    .filter(p -> {
                        int idx  = p.neighbors().mapToInt(x -> unlitPixels.contains(x) ? 0 : 1).reduce(0, (a,b) -> (a << 1) + b);
                        return enhancement.get(idx);
                    })
                    .collect(Collectors.toSet());
        } else {
            // Standard steps
            return step(enhancement, step(enhancement, litPixels));
        }
    }

    @Part2
    public static long part2(Input input) {
        Set<Point> litPixels = new HashSet<>(input.litPixels);

        for (int i = 0; i < 50; i += 2) {
            litPixels = doubleStep(input.enhancement, litPixels);
        }

        return litPixels.size();
    }

    @InputParser
    public static Input parse(Stream<String> stream) {
        List<String> strings = stream.toList();

        List<Boolean> enhancement = strings.get(0).chars().mapToObj(c -> c == '#').toList();
        List<Point> litPixels = new ArrayList<>();

        for (int i = 2; i < strings.size(); i++) {
            String s = strings.get(i);
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '#') {
                    litPixels.add(new Point(j, i - 2));
                }
            }
        }

        return new Input(enhancement, litPixels);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day20.txt"));
        Input l = parse(br.lines());

        System.out.println(part1(l));
        while (true) {
            System.out.println(part2(l));
        }
    }
}
