package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Day01 {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day01.txt"));
        List<Integer> l = br.lines().map(Integer::parseInt).toList();

        int increased = 0;
        for (int i = 1; i < l.size(); i++) {
            if (l.get(i - 1) < l.get(i)) {
                increased++;
            }
        }
        System.out.println(increased);

        // Comparing A+B+C and B+C+D is the same as comparing A and D directly
        int increased2 = 0;
        for (int i = 3; i < l.size(); i++) {
            if (l.get(i - 3) < l.get(i)) {
                increased2++;
            }
        }
        System.out.println(increased2);
    }
}
