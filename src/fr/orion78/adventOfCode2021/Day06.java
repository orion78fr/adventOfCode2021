package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day06 {
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day06.txt"));
        List<String> l = br.lines().toList();

        Map<Integer, Long> m = Arrays.stream(l.get(0).split(",")).collect(Collectors.toMap(Integer::parseInt, x -> 1L, Long::sum));

        for (int curDay = 0; curDay < 80; curDay++) {
            long curFish = m.getOrDefault(curDay, 0L);
            m.put(curDay + 7, m.getOrDefault(curDay + 7, 0L) + curFish);
            m.put(curDay + 9, m.getOrDefault(curDay + 9, 0L) + curFish);
        }

        long fish = m.entrySet().stream()
                .filter(e -> e.getKey() >= 80)
                .mapToLong(Map.Entry::getValue)
                .sum();

        System.out.println(fish);

        for (int curDay = 80; curDay < 256; curDay++) {
            long curFish = m.getOrDefault(curDay, 0L);
            m.put(curDay + 7, m.getOrDefault(curDay + 7, 0L) + curFish);
            m.put(curDay + 9, m.getOrDefault(curDay + 9, 0L) + curFish);
        }

        long fish2 = m.entrySet().stream()
                .filter(e -> e.getKey() >= 256)
                .mapToLong(Map.Entry::getValue)
                .sum();

        System.out.println(fish2);
    }
}
