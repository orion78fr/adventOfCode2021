package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class Day10 {
    public enum ChunkType {
        PARENTHESIS('(', ')', 3, 1),
        SQUARE_BRACKET('[', ']', 57, 2),
        CURLY_BRACKET('{', '}', 1197, 3),
        ANGLE_BRACKET('<', '>', 25137, 4);

        private final char opening;
        private final char closing;
        private final long corruptedValue;
        private final long autocompleteValue;

        private static final Map<Character, ChunkType> fromChar = new HashMap<>();

        ChunkType(char opening, char closing, long corruptedValue, long autocompleteValue) {
            this.opening = opening;
            this.closing = closing;
            this.corruptedValue = corruptedValue;
            this.autocompleteValue = autocompleteValue;
        }

        static {
            for (ChunkType c : ChunkType.values()) {
                fromChar.put(c.opening, c);
                fromChar.put(c.closing, c);
            }
        }

        public static ChunkType getFromChar(char c) {
            return fromChar.get(c);
        }
    }

    @Part1
    public static long part1(List<String> input) {
        long total = 0;

        next_line: for (String s : input) {
            Deque<Character> stack = new ArrayDeque<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                ChunkType ct = ChunkType.getFromChar(c);

                if (c == ct.opening) {
                    stack.addLast(ct.closing);
                } else {
                    Character expected = stack.removeLast();
                    if (c != expected) {
                        // Wrong closing
                        total += ct.corruptedValue;
                        continue next_line;
                    }
                }
            }
        }

        return total;
    }

    @Part2
    public static long part2(List<String> input) {
        List<Long> scores = new ArrayList<>();

        next_line: for (String s : input) {
            Deque<Character> stack = new ArrayDeque<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                ChunkType ct = ChunkType.getFromChar(c);

                if (c == ct.opening) {
                    stack.addLast(ct.closing);
                } else {
                    Character expected = stack.removeLast();
                    if (c != expected) {
                        // Wrong closing
                        continue next_line;
                    }
                }
            }

            long score = 0;
            while(!stack.isEmpty()) {
                Character c = stack.removeLast();
                ChunkType ct = ChunkType.getFromChar(c);
                score = score * 5 + ct.autocompleteValue;
            }
            scores.add(score);
        }

        Collections.sort(scores);

        return scores.get(scores.size() / 2);
    }

    @InputParser
    public static List<String> parse(Stream<String> stream) {
        return stream.toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day10.txt"));
        List<String> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
