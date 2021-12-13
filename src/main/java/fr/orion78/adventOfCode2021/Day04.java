package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day
public class Day04 {
    public record Bingo(List<Integer> drawnNumbers, List<Board> boards) {
    }

    public static class Board {
        final List<List<Integer>> boardNumbers;

        public Board(List<String> board) {
            boardNumbers = board.stream()
                    .map(s -> s.split(" "))
                    .map(s -> Arrays.stream(s).filter(x -> !x.isEmpty()).map(Integer::parseInt).toList())
                    .collect(Collectors.toList());
        }

        public boolean isBingo(List<Integer> drawn) {
            // Horizontal
            for (List<Integer> line : boardNumbers) {
                if (drawn.containsAll(line)) {
                    return true;
                }
            }

            // Vertical
            columns:
            for (int i = 0; i < boardNumbers.size(); i++) {
                for (List<Integer> line : boardNumbers) {
                    if (!drawn.contains(line.get(i))) {
                        continue columns;
                    }
                }
                return true;
            }
            return false;
        }

        public int score(List<Integer> drawn) {
            int sum = boardNumbers.stream()
                    .flatMap(Collection::stream)
                    .mapToInt(x -> x)
                    .filter(x -> !drawn.contains(x))
                    .sum();

            return sum * drawn.get(drawn.size() - 1);
        }
    }

    @Part1
    public static long part1(Bingo input) {
        for (int i = 5; i <= input.drawnNumbers.size(); i++) {
            List<Integer> drawn = input.drawnNumbers.subList(0, i);

            for (int j = 0; j < input.boards.size(); j++) {
                Board board = input.boards.get(j);

                if (board.isBingo(drawn)) {
                    return board.score(drawn);
                }
            }
        }

        throw new RuntimeException();
    }

    @Part2
    public static long part2(Bingo input) {
        List<Board> boards = new ArrayList<>(input.boards);
        for (int i = 5; i <= input.drawnNumbers.size(); i++) {
            List<Integer> drawn = input.drawnNumbers.subList(0, i);

            for (int j = 0; j < boards.size(); j++) {
                Board board = boards.get(j);

                if (board.isBingo(drawn)) {
                    boards.remove(board);
                    j--;

                    if (boards.size() == 0) {
                        return board.score(drawn);
                    }
                }
            }
        }

        throw new RuntimeException();
    }

    @InputParser
    public static Bingo parse(Stream<String> stream) {
        List<String> l = stream.toList();

        List<Integer> drawnNumbers = Arrays.stream(l.get(0).split(",")).map(Integer::parseInt).toList();
        List<Board> boards = new ArrayList<>(l.size() / 6);

        // Parse the boards
        for (int i = 2; i < l.size(); i += 6) {
            boards.add(new Board(l.subList(i, i + 5)));
        }

        return new Bingo(drawnNumbers, boards);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day04.txt"));
        Bingo l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
