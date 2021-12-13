package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day
public class Day08 {
    @Part1
    public static long part1(List<String> input) {
        return input.stream()
                .map(x -> x.split("\\|")[1])
                .flatMap(x -> Arrays.stream(x.trim().split(" ")))
                .filter(x -> {
                    int length = x.length();
                    return length == 2 || length == 3 || length == 4 || length == 7;
                })
                .count();
    }

    @Part2
    public static long part2(List<String> input) {
        return input.stream()
                .map(x -> x.replace(" | ", " ").split(" "))
                .mapToInt(Day08::decodeSignal)
                .sum();
    }

    @InputParser
    public static List<String> parse(Stream<String> stream) {
        return stream.toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day08.txt"));
        List<String> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }

    private static Set<Character> strToSet(String str) {
        return str.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    private static <R> long intersectCount(Set<R> a, Set<R> b) {
        return a.stream().filter(b::contains).count();
    }

    /*
                if (s.length() == 2) {
                    // 1 (cf)
                } else if (s.length() == 3) {
                    // 7 (acf)
                } else if (s.length() == 4) {
                    // 4 (bcdf)
                } else if (s.length() == 5) {
                    // 2 (acdeg)
                    // 3 (acdfg)
                    // 5 (abdfg)
                } else if (s.length() == 6) {
                    // 0 (abcefg)
                    // 6 (abdefg)
                    // 9 (abcdfg)
                } else if (s.length() == 7) {
                    // 8 (abcdefg)
                }
     */

    private static int decodeSignal(String[] digits) {
        Map<Set<Character>, Integer> correspondence = new HashMap<>();

        String one = Arrays.stream(digits).filter(s -> s.length() == 2).findAny().get();
        Set<Character> oneSet = strToSet(one);
        correspondence.put(oneSet, 1);

        String seven = Arrays.stream(digits).filter(s -> s.length() == 3).findAny().get();
        Set<Character> sevenSet = strToSet(seven);
        correspondence.put(sevenSet, 7);

        String four = Arrays.stream(digits).filter(s -> s.length() == 4).findAny().get();
        Set<Character> fourSet = strToSet(four);
        correspondence.put(fourSet, 4);

        String eight = Arrays.stream(digits).filter(s -> s.length() == 7).findAny().get();
        Set<Character> eightSet = strToSet(eight);
        correspondence.put(eightSet, 8);

        Set<Set<Character>> fiveLengths = Arrays.stream(digits).filter(s -> s.length() == 5)
                .map(Day08::strToSet)
                .collect(Collectors.toSet());

        for (Set<Character> s : fiveLengths) {
            if (intersectCount(s, sevenSet) == 3) {
                correspondence.put(s, 3);
            } else {
                if (intersectCount(s, fourSet) == 3) {
                    correspondence.put(s, 5);
                } else {
                    correspondence.put(s, 2);
                }
            }
        }

        Set<Set<Character>> sixLengths = Arrays.stream(digits).filter(s -> s.length() == 6)
                .map(Day08::strToSet)
                .collect(Collectors.toSet());

        for (Set<Character> s : sixLengths) {
            if (intersectCount(s, fourSet) == 4) {
                correspondence.put(s, 9);
            } else {
                if (intersectCount(s, sevenSet) == 3) {
                    correspondence.put(s, 0);
                } else {
                    correspondence.put(s, 6);
                }
            }
        }

        return correspondence.get(strToSet(digits[digits.length - 1]))
                + 10 * correspondence.get(strToSet(digits[digits.length - 2]))
                + 100 * correspondence.get(strToSet(digits[digits.length - 3]))
                + 1000 * correspondence.get(strToSet(digits[digits.length - 4]));
    }
}
