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
    public record Pair(String a, String b) {
    }

    public static boolean isSmallCave(String cave) {
        return cave.toLowerCase().equals(cave);
    }

    @Part1
    public static long part1(Map<String, Set<String>> input) {
        return visit(input, "start", Collections.emptySet(), true);
    }

    public static long visit(Map<String, Set<String>> input, String next, Set<String> visited, boolean visitedTwice) {
        if (next.equals("end")) {
            return 1;
        }

        Set<String> newVisited = new HashSet<>(visited);
        newVisited.add(next);

        long paths = 0;

        Set<String> neighbors = input.get(next);
        for (String neighbor : neighbors) {
            if (isSmallCave(neighbor) && visited.contains(neighbor)) {
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
    public static long part2(Map<String, Set<String>> input) {
        return visit(input, "start", Collections.emptySet(), false);
    }

    @InputParser
    public static Map<String, Set<String>> parse(Stream<String> stream) {
        return stream
                .flatMap(s -> {
                    String[] split = s.split("-");
                    return Stream.of(new Pair(split[0], split[1]), new Pair(split[1], split[0]));
                })
                .collect(Collectors.toMap(Pair::a, p -> Set.of(p.b), (a, b) -> {
                    Set<String> s = new HashSet<>(a.size() + b.size());
                    s.addAll(a);
                    s.addAll(b);
                    s.remove("start"); // Can't go back to start
                    return s;
                }));
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day12.txt"));
        Map<String, Set<String>> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
