package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Day
public class Day24 {
    private static final Pattern num = Pattern.compile("^-?\\d+$");

    @Part1
    public static long part1(List<String[]> program) {
        return Long.parseLong(LongStream.iterate(99_999_999_999_999L, i -> i >= 0, i -> i - 1)
                .parallel()
                .mapToObj(Long::toString)
                .filter(i -> !i.contains("0"))
                .filter(i -> execute(i, program).get("z") == 0)
                .findFirst().get());
    }

    public static Map<String, Long> execute(String input, List<String[]> program) {
        Map<String, Long> registers = new HashMap<>();
        registers.put("w", 0L);
        registers.put("x", 0L);
        registers.put("y", 0L);
        registers.put("z", 0L);

        List<Long> in = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            in.add((long) (input.charAt(i) - '0'));
        }

        int nextIn = 0;

        for (String[] instruction : program) {
            Long rOp = null;
            if (instruction.length > 2 && !registers.containsKey(instruction[2])) {
                try {
                    rOp = Long.parseLong(instruction[2]);
                } catch (NumberFormatException ignored) {
                }
            }

            switch (instruction[0]) {
                case "inp" -> registers.put(instruction[1], in.get(nextIn++));
                case "add" -> registers.put(instruction[1], registers.get(instruction[1]) + (rOp == null ? registers.get(instruction[2]) : rOp));
                case "mul" -> registers.put(instruction[1], registers.get(instruction[1]) * (rOp == null ? registers.get(instruction[2]) : rOp));
                case "div" -> registers.put(instruction[1], registers.get(instruction[1]) / (rOp == null ? registers.get(instruction[2]) : rOp));
                case "mod" -> registers.put(instruction[1], registers.get(instruction[1]) % (rOp == null ? registers.get(instruction[2]) : rOp));
                case "eql" -> registers.put(instruction[1], registers.get(instruction[1]).equals(rOp == null ? registers.get(instruction[2]) : rOp) ? 1L : 0L);
            }
        }

        return registers;
    }

    @Part2
    public static long part2(List<String[]> program) {
        return -1;
    }

    @InputParser
    public static List<String[]> parse(Stream<String> stream) {
        return stream.map(s -> s.split(" ")).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day24.txt"));
        List<String[]> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
