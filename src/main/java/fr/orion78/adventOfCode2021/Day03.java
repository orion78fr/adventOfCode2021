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

@Day
public class Day03 {
    @Part1
    public static long part1(List<String> input) {
        int lines = input.size();
        int wordLength = input.get(0).length();
        int[] oneCounts = new int[wordLength];

        for (String s : input) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '1') {
                    oneCounts[i]++;
                }
            }
        }

        long gamma = 0;
        long epsilon = 0;
        for (int c : oneCounts) {
            gamma <<= 1;
            epsilon <<= 1;
            if (c > lines / 2) {
                gamma += 1;
            } else {
                epsilon += 1;
            }
        }

        return gamma * epsilon;
    }

    @Part2
    public static long part2(List<String> input) {
        int wordLength = input.get(0).length();

        int wordPos = 0;
        List<String> oxygenGeneratorRatingList = new ArrayList<>(input);
        while (oxygenGeneratorRatingList.size() != 1) {
            int oneCount = 0;

            for (String s : oxygenGeneratorRatingList) {
                oneCount += s.charAt(wordPos % wordLength) == '1' ? 1 : 0;
            }

            int finalWordPos = wordPos;
            if ((double) oneCount >= (double) oxygenGeneratorRatingList.size() / 2) {
                // Keep ones if equal
                oxygenGeneratorRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '0');
            } else {
                oxygenGeneratorRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '1');
            }
            wordPos++;
        }

        wordPos = 0;
        List<String> co2ScrubberRatingList = new ArrayList<>(input);
        while (co2ScrubberRatingList.size() != 1) {
            int oneCount = 0;

            for (String s : co2ScrubberRatingList) {
                oneCount += s.charAt(wordPos % wordLength) == '1' ? 1 : 0;
            }

            int finalWordPos = wordPos;
            if ((double) oneCount >= (double) co2ScrubberRatingList.size() / 2) {
                co2ScrubberRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '1');
            } else {
                // Keep zeroes if equal
                co2ScrubberRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '0');
            }
            wordPos++;
        }

        return Long.parseLong(oxygenGeneratorRatingList.get(0), 2) * Long.parseLong(co2ScrubberRatingList.get(0), 2);
    }

    @InputParser
    public static List<String> parse(Stream<String> stream) {
        return stream.toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day03.txt"));
        List<String> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
