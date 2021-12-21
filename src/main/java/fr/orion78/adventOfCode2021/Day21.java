package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Day
public class Day21 {
    public record StartingPoints(int player1, int player2) {

    }

    public record Res(long player1, long player2) {

    }

    public record Gamestate(long p1Score, long p2Score, long p1Pos, long p2Pos, boolean p1Turn) {

    }

    @Part1
    public static long part1(StartingPoints input) {
        long p1Score = 0;
        long p2Score = 0;
        long dice = 0;
        long p1Pos = input.player1 - 1;
        long p2Pos = input.player2 - 1;

        boolean p1Turn = true;

        while (p1Score < 1000 && p2Score < 1000) {
            long rolledDices = (dice % 100) + ((dice + 1) % 100) + ((dice + 2) % 100) + 3;
            dice += 3;

            if (p1Turn) {
                p1Pos = (p1Pos + rolledDices) % 10;
                p1Score += p1Pos + 1;
            } else {
                p2Pos = (p2Pos + rolledDices) % 10;
                p2Score += p2Pos + 1;
            }

            p1Turn = !p1Turn;
        }
        return dice * (p1Score >= 1000 ? p2Score : p1Score);
    }

    @Part2
    public static long part2(StartingPoints input) {
        Res res = game(new HashMap<>(), 0, 0, input.player1 - 1, input.player2 - 1, true);

        return res.player1 > res.player2 ? res.player1 : res.player2;
    }

    public static Res game(Map<Gamestate, Res> memo, long p1Score, long p2Score, long p1Pos, long p2Pos, boolean p1Turn) {
        if (p1Score >= 21) {
            return new Res(1, 0);
        } else if (p2Score >= 21) {
            return new Res(0, 1);
        }

        Gamestate gs = new Gamestate(p1Score, p2Score, p1Pos, p2Pos, p1Turn);

        if (memo.containsKey(gs)) {
            return memo.get(gs);
        }

        Res res3, res4, res5, res6, res7, res8, res9;

        if (p1Turn) {
            res3 = game(memo, p1Score + 1 + ((p1Pos + 3) % 10), p2Score, (p1Pos + 3) % 10, p2Pos, false);
            res4 = game(memo, p1Score + 1 + ((p1Pos + 4) % 10), p2Score, (p1Pos + 4) % 10, p2Pos, false);
            res5 = game(memo, p1Score + 1 + ((p1Pos + 5) % 10), p2Score, (p1Pos + 5) % 10, p2Pos, false);
            res6 = game(memo, p1Score + 1 + ((p1Pos + 6) % 10), p2Score, (p1Pos + 6) % 10, p2Pos, false);
            res7 = game(memo, p1Score + 1 + ((p1Pos + 7) % 10), p2Score, (p1Pos + 7) % 10, p2Pos, false);
            res8 = game(memo, p1Score + 1 + ((p1Pos + 8) % 10), p2Score, (p1Pos + 8) % 10, p2Pos, false);
            res9 = game(memo, p1Score + 1 + ((p1Pos + 9) % 10), p2Score, (p1Pos + 9) % 10, p2Pos, false);
        } else {
            res3 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 3) % 10), p1Pos, (p2Pos + 3) % 10, true);
            res4 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 4) % 10), p1Pos, (p2Pos + 4) % 10, true);
            res5 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 5) % 10), p1Pos, (p2Pos + 5) % 10, true);
            res6 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 6) % 10), p1Pos, (p2Pos + 6) % 10, true);
            res7 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 7) % 10), p1Pos, (p2Pos + 7) % 10, true);
            res8 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 8) % 10), p1Pos, (p2Pos + 8) % 10, true);
            res9 = game(memo, p1Score, p2Score + 1 + ((p2Pos + 9) % 10), p1Pos, (p2Pos + 9) % 10, true);
        }

        Res res = new Res(
                res3.player1
                        + 3 * res4.player1
                        + 6 * res5.player1
                        + 7 * res6.player1
                        + 6 * res7.player1
                        + 3 * res8.player1
                        + res9.player1,
                res3.player2
                        + 3 * res4.player2
                        + 6 * res5.player2
                        + 7 * res6.player2
                        + 6 * res7.player2
                        + 3 * res8.player2
                        + res9.player2);
        memo.put(gs, res);

        return res;
    }

    @InputParser
    public static StartingPoints parse(Stream<String> stream) {
        List<String> strings = stream.toList();

        return new StartingPoints(Integer.parseInt(strings.get(0).split(": ")[1]),
                Integer.parseInt(strings.get(1).split(": ")[1]));
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day21.txt"));
        StartingPoints l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
