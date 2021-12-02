package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Day02 {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day02.txt"));
        List<String[]> l = br.lines().map(x -> x.split(" ")).toList();

        long hPos = 0;
        long depth = 0;

        for (String[] s : l) {
            long dist = Long.parseLong(s[1]);

            switch (s[0]) {
                case "forward" -> hPos += dist;
                case "down" -> depth += dist;
                case "up" -> depth -= dist;
            }
        }

        System.out.println(hPos * depth);

        long aim = 0;
        hPos = 0;
        depth = 0;

        for (String[] s : l) {
            long dist = Long.parseLong(s[1]);

            switch (s[0]) {
                case "down" -> aim += dist;
                case "up" -> aim -= dist;
                case "forward" -> {
                    hPos += dist;
                    depth += (aim * dist);
                }
            }
        }

        System.out.println(hPos * depth);
    }
}
