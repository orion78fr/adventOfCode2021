package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//@Day
public class Day23 {
    @Part1
    public static long part1(List<List<Integer>> input) {
        List<Integer> hallway = new ArrayList<>();
        for (int i = 0; i < 4+input.size()-1; i++) {
            hallway.add(0);
        }
        return -1;
    }

    //public static long

    @Part2
    public static long part2(List<List<Integer>> input) {
        return -1;
    }

    @InputParser
    public static List<List<Integer>> parse(Stream<String> stream) {
        List<String> strings = stream.map(s -> s.replaceAll("[#. ]", ""))
                .filter(s -> !s.isEmpty())
                .toList();

        int nbRooms = strings.get(0).length();
        List<List<Integer>> input = new ArrayList<>(nbRooms);
        for (int i = 0; i < nbRooms; i++) {
            input.add(new ArrayList<>(strings.size()));
        }

        for (String s : strings) {
            for (int i = 0; i < s.length(); i++) {
                input.get(i).add(s.charAt(i) - 'A' + 1);
            }
        }

        return input;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day23.txt"));
        List<List<Integer>> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
