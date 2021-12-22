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

    public record Result(long part1, long part2){

    }

    public record Input(List<Boolean> enhancement, List<Point> litPixels) {

    }

    public static Set<Point> step(List<Boolean> enhancement, Set<Point> litPixels) {
        Set<Point> relitPixels = litPixels.stream()
                .flatMap(Point::neighbors) // Consider all points near lit pixels
                .collect(Collectors.toSet());

        relitPixels.removeIf(p -> {
            int idx = p.neighbors().mapToInt(x -> litPixels.contains(x) ? 1 : 0).reduce(0, (a, b) -> (a << 1) + b);
            return !enhancement.get(idx);
        });

        return relitPixels;
    }

    public static Set<Point> doubleStep(List<Boolean> enhancement, Set<Point> litPixels) {
        if (enhancement.get(0) && !enhancement.get(enhancement.size() - 1)) {
            // Alternating on / off
            Set<Point> unlitPixels = litPixels.stream()
                    .flatMap(Point::neighbors) // Consider all points near lit pixels
                    .collect(Collectors.toSet());
            unlitPixels.removeIf(p -> {
                int idx = p.neighbors().mapToInt(x -> litPixels.contains(x) ? 1 : 0).reduce(0, (a, b) -> (a << 1) + b);
                return enhancement.get(idx);
            });

            Set<Point> relitPixels = unlitPixels.stream()
                    .flatMap(Point::neighbors) // Consider all points near unlit pixels
                    .collect(Collectors.toSet());
            relitPixels.removeIf(p -> {
                int idx = p.neighbors().mapToInt(x -> unlitPixels.contains(x) ? 0 : 1).reduce(0, (a, b) -> (a << 1) + b);
                return !enhancement.get(idx);
            });
            return relitPixels;
        } else {
            // Standard steps
            return step(enhancement, step(enhancement, litPixels));
        }
    }

    @Part1(bothParts = true)
    public static Result bothParts(Input input) {
        Set<Point> litPixels = new HashSet<>(input.litPixels);

        litPixels = doubleStep(input.enhancement, litPixels);
        int part1 = litPixels.size();

        for (int i = 2; i < 50; i += 2) {
            litPixels = doubleStep(input.enhancement, litPixels);
        }

        return new Result(part1, litPixels.size());
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

        while (true) {
            System.out.println(bothParts(l));
        }
    }
}
