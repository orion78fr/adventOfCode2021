package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Stream;

@Day
public class Day17 {
    public record Target(int xMin, int xMax, int yMin, int yMax) {
    }

    @Part1
    public static long part1(Target input) {
        int velocityY = Math.abs(input.yMin) - 1;
        return velocityY * (velocityY + 1) / 2;
    }

    @Part2
    public static long part2(Target input) {
        int maxVelocityX = input.xMax;

        int minVelocityY = input.yMin;
        int maxVelocityY = Math.abs(input.yMin) - 1;

        long count = 0;

        for (int initVelX = 0; initVelX <= maxVelocityX; initVelX++) {
            nextPoint:
            for (int initVelY = minVelocityY; initVelY <= maxVelocityY; initVelY++) {
                int curX = 0;
                int curY = 0;
                int velX = initVelX;
                int velY = initVelY;

                while (curX <= input.xMax && curY >= input.yMin) { // Overshoot
                    curX += velX;
                    curY += velY;

                    velX = Math.max(0, velX - 1);
                    velY -= 1;

                    if (curX >= input.xMin && curX <= input.xMax && curY >= input.yMin && curY <= input.yMax) {
                        // Reached target
                        count++;
                        continue nextPoint;
                    }
                }
            }
        }

        return count;
    }

    @InputParser
    public static Target parse(Stream<String> stream) {
        List<String> str = stream.toList();

        String[] split = str.get(0).substring("target area: x=".length()).split(",");
        String[] splitX = split[0].split("\\.\\.");
        String[] splitY = split[1].substring(" y=".length()).split("\\.\\.");

        return new Target(Integer.parseInt(splitX[0]), Integer.parseInt(splitX[1]),
                Integer.parseInt(splitY[0]), Integer.parseInt(splitY[1]));
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day17.txt"));
        Target l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));

        //System.out.println(part2(parse(Stream.of("target area: x=20..30, y=-10..-5"))));
    }
}
