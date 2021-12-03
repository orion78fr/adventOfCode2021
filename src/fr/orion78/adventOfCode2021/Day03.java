package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day03 {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day03.txt"));
        List<String> l = br.lines().toList();
        //List<String> l = List.of("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001", "00010", "01010");

        int lines = l.size();
        int wordLength = l.get(0).length();
        int[] oneCounts = new int[wordLength];

        for (String s : l) {
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

        System.out.println(gamma * epsilon);

        int wordPos = 0;
        List<String> oxygenGeneratorRatingList = new ArrayList<>(l);
        while (oxygenGeneratorRatingList.size() != 1) {
            int oneCount = 0;

            for (String s : oxygenGeneratorRatingList) {
                oneCount += s.charAt(wordPos % wordLength) == '1' ? 1 : 0;
            }

            int finalWordPos = wordPos;
            if ((double)oneCount >= (double)oxygenGeneratorRatingList.size() / 2) {
                // Keep ones if equal
                oxygenGeneratorRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '0');
            } else {
                oxygenGeneratorRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '1');
            }
            wordPos++;
        }

        wordPos = 0;
        List<String> co2ScrubberRatingList = new ArrayList<>(l);
        while (co2ScrubberRatingList.size() != 1) {
            int oneCount = 0;

            for (String s : co2ScrubberRatingList) {
                oneCount += s.charAt(wordPos % wordLength) == '1' ? 1 : 0;
            }

            int finalWordPos = wordPos;
            if ((double)oneCount >= (double)co2ScrubberRatingList.size() / 2) {
                co2ScrubberRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '1');
            } else {
                // Keep zeroes if equal
                co2ScrubberRatingList.removeIf(s -> s.charAt(finalWordPos % wordLength) == '0');
            }
            wordPos++;
        }

        System.out.println(Long.parseLong(oxygenGeneratorRatingList.get(0), 2) * Long.parseLong(co2ScrubberRatingList.get(0), 2));
    }
}
