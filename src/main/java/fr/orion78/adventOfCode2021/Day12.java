package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day
public class Day12 {
    private static final Cave START_CAVE = new Cave("start", true);
    private static final Cave END_CAVE = new Cave("end", true);

    public record Pair(Cave a, Cave b) {
    }

    public record Cave(String name, boolean isSmall) {
    }

    public static boolean isSmallCave(String cave) {
        return cave.toLowerCase().equals(cave);
    }

    @Part1
    public static long part1(Map<Cave, Set<Cave>> input) {
        return visit(input, START_CAVE, Collections.emptySet(), true);
    }

    public static long visit(Map<Cave, Set<Cave>> input, Cave next, Set<Cave> visited, boolean visitedTwice) {
        if (next.equals(END_CAVE)) {
            return 1;
        }

        Set<Cave> newVisited = new HashSet<>(visited);
        newVisited.add(next);

        long paths = 0;

        Set<Cave> neighbors = input.get(next);
        for (Cave neighbor : neighbors) {
            if (neighbor.isSmall && visited.contains(neighbor)) {
                if (!visitedTwice) {
                    paths += visit(input, neighbor, newVisited, true);
                }
            } else {
                paths += visit(input, neighbor, newVisited, visitedTwice);
            }
        }
        return paths;
    }

    @Part2
    public static long part2(Map<Cave, Set<Cave>> input) {
        return visit(input, START_CAVE, Collections.emptySet(), false);
    }

    @InputParser
    public static Map<Cave, Set<Cave>> parse(Stream<String> stream) {
        return stream
                .map(s -> {
                    String[] split = s.split("-");
                    return new Pair(new Cave(split[0], isSmallCave(split[0])), new Cave(split[1], isSmallCave(split[1])));
                })
                .flatMap(p -> Stream.of(p, new Pair(p.b, p.a)))
                .collect(Collectors.toMap(Pair::a, p -> Set.of(p.b), (a, b) -> {
                    Set<Cave> s = new HashSet<>(a.size() + b.size());
                    s.addAll(a);
                    s.addAll(b);
                    s.remove(START_CAVE); // Can't go back to start
                    return s;
                }));
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day12.txt"));
        Map<Cave, Set<Cave>> l = parse(br.lines());

        while (true) {
            System.out.println(part1(l));
            System.out.println(part2(l));
        }
    }
}
