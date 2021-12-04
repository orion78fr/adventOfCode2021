package fr.orion78.adventOfCode2021;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 {
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

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day04.txt"));
        List<String> l = br.lines().toList();

        List<Integer> drawnNumbers = Arrays.stream(l.get(0).split(",")).map(Integer::parseInt).toList();
        List<Board> boards = new ArrayList<>(l.size() / 6);

        // Parse the boards
        for (int i = 2; i < l.size(); i += 6) {
            boards.add(new Board(l.subList(i, i + 5)));
        }

        Board winner = null;
        // Check if one wins
        for (int i = 5; i <= drawnNumbers.size(); i++) {
            List<Integer> drawn = drawnNumbers.subList(0, i);

            for (int j = 0; j < boards.size(); j++) {
                Board board = boards.get(j);

                if (board.isBingo(drawn)) {
                    if (winner == null) {
                        winner = board;
                        System.out.println(board.score(drawn));
                    }

                    boards.remove(board);
                    j--;

                    if (boards.size() == 0) {
                        System.out.println(board.score(drawn));
                    }
                }
            }
        }
    }
}
