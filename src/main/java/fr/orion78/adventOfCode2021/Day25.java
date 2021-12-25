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
public class Day25 {
    public record Point(int x, int y) {

    }

    public record Input(int sizeX, int sizeY, Map<Point, Character> cucumbers) {

    }

    @Part1(bothParts = true)
    public static long part1(Input input) {
        long steps = 0;

        boolean movedRight;
        boolean movedDown;

        Map<Point, Character> m = input.cucumbers;

        do {
            steps++;

            StepRes r = moveRight(input.sizeX, input.sizeY, m);
            movedRight = r.moved;
            m = r.res;

            StepRes d = moveDown(input.sizeX, input.sizeY, m);
            movedDown = d.moved;
            m = d.res;
        } while (movedRight || movedDown);

        return steps;
    }

    public record StepRes(boolean moved, Map<Point, Character> res) {

    }

    public static StepRes moveRight(int sizeX, int sizeY, Map<Point, Character> cucumbers) {
        boolean moved = false;
        Map<Point, Character> res = new HashMap<>(cucumbers.size());

        for (var e : cucumbers.entrySet()) {
            if (e.getValue() != '>') {
                res.put(e.getKey(), e.getValue());
            } else {
                Point src = e.getKey();
                Point dst = new Point((src.x + 1) % sizeX, src.y);

                if (cucumbers.containsKey(dst)) {
                    res.put(src, e.getValue());
                } else {
                    moved = true;
                    res.put(dst, e.getValue());
                }
            }
        }

        return new StepRes(moved, res);
    }

    public static StepRes moveDown(int sizeX, int sizeY, Map<Point, Character> cucumbers) {
        boolean moved = false;
        Map<Point, Character> res = new HashMap<>(cucumbers.size());

        for (var e : cucumbers.entrySet()) {
            if (e.getValue() != 'v') {
                res.put(e.getKey(), e.getValue());
            } else {
                Point src = e.getKey();
                Point dst = new Point(src.x, (src.y + 1) % sizeY);

                if (cucumbers.containsKey(dst)) {
                    res.put(src, e.getValue());
                } else {
                    moved = true;
                    res.put(dst, e.getValue());
                }
            }
        }

        return new StepRes(moved, res);
    }

    @InputParser
    public static Input parse(Stream<String> stream) {
        List<String> strings = stream.toList();
        Map<Point, Character> m = new HashMap<>();
        for (int y = 0; y < strings.size(); y++) {
            String s = strings.get(y);
            for (int x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                if (c != '.') {
                    m.put(new Point(x, y), c);
                }
            }
        }
        return new Input(strings.get(0).length(), strings.size(), m);
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day25.txt"));
        Input l = parse(br.lines());

        while (true)
        System.out.println(part1(l));
    }
}
