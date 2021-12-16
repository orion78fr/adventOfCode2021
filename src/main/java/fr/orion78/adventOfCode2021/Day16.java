package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Day
public class Day16 {
    @Part1
    public static long part1(List<Integer> input) {
        Iterator<Integer> it = input.iterator();

        return decodePacketAndGetVersion(it);
    }

    public static long decodePacketAndGetVersion(Iterator<Integer> it){
        long version = takeToLong(it, 3);
        long type = takeToLong(it, 3);

        long totalVer = version;

        if (type == 4) {
            // Literal
            long literal = takeEncodedNumber(it);
        } else {
            // Operator
            long lengthType = takeToLong(it, 1);
            if (lengthType == 0){
                // Total length of subpackets
                int length = (int) takeToLong(it, 15);

                List<Integer> l = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    l.add(it.next());
                }
                Iterator<Integer> newIt = l.iterator();

                while (newIt.hasNext()){
                    totalVer += decodePacketAndGetVersion(newIt);
                }
            } else {
                // Number of subpackets
                long length = takeToLong(it, 11);

                for (long i = 0; i < length; i++) {
                    totalVer += decodePacketAndGetVersion(it);
                }
            }
        }

        return totalVer;
    }

    public static long takeToLong(Iterator<Integer> it, int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(it.next());
        }
        return Long.parseLong(sb.toString(), 2);
    }

    public static long takeEncodedNumber(Iterator<Integer> it) {
        StringBuilder sb = new StringBuilder();

        boolean hasMore;
        do {
            hasMore = it.next() == 1;
            for (int i = 0; i < 4; i++) {
                sb.append(it.next());
            }
        } while (hasMore);

        return Long.parseLong(sb.toString(), 2);
    }

    @Part2
    public static long part2(List<Integer> input) {
        Iterator<Integer> it = input.iterator();

        return decodePacket(it);
    }

    public static long decodePacket(Iterator<Integer> it){
        long version = takeToLong(it, 3);
        long type = takeToLong(it, 3);

        if (type == 4) {
            // Literal
            return takeEncodedNumber(it);
        } else {
            // Operator
            long lengthType = takeToLong(it, 1);
            List<Long> subPacketResults = new ArrayList<>();

            if (lengthType == 0){
                // Total length of subpackets
                int length = (int) takeToLong(it, 15);

                List<Integer> l = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    l.add(it.next());
                }
                Iterator<Integer> newIt = l.iterator();

                while (newIt.hasNext()){
                    subPacketResults.add(decodePacket(newIt));
                }
            } else {
                // Number of subpackets
                long length = takeToLong(it, 11);

                for (long i = 0; i < length; i++) {
                    subPacketResults.add(decodePacket(it));
                }
            }

            switch ((int)type){
                case 0 -> {
                    // Sum
                    return subPacketResults.stream()
                            .mapToLong(x -> x)
                            .sum();
                }
                case 1 -> {
                    // Product
                    return subPacketResults.stream()
                            .mapToLong(x -> x)
                            .reduce(1, (a,b) -> a * b);
                }
                case 2-> {
                    // Min
                    return subPacketResults.stream()
                            .mapToLong(x -> x)
                            .min().getAsLong();
                }
                case 3 -> {
                    // Max
                    return subPacketResults.stream()
                            .mapToLong(x -> x)
                            .max().getAsLong();
                }
                case 5 -> {
                    // >
                    return subPacketResults.get(0) > subPacketResults.get(1) ? 1 : 0;
                }
                case 6 -> {
                    // <
                    return subPacketResults.get(0) < subPacketResults.get(1) ? 1 : 0;
                }
                case 7 -> {
                    // =
                    return subPacketResults.get(0).longValue() == subPacketResults.get(1).longValue() ? 1 : 0;
                }
                default -> throw new RuntimeException();
            }
        }
    }

    @InputParser
    public static List<Integer> parse(Stream<String> stream) {
        return stream
                .flatMapToInt(String::chars)
                .flatMap(c -> switch (c) {
                    case '0' -> "0000".chars();
                    case '1' -> "0001".chars();
                    case '2' -> "0010".chars();
                    case '3' -> "0011".chars();
                    case '4' -> "0100".chars();
                    case '5' -> "0101".chars();
                    case '6' -> "0110".chars();
                    case '7' -> "0111".chars();
                    case '8' -> "1000".chars();
                    case '9' -> "1001".chars();
                    case 'A' -> "1010".chars();
                    case 'B' -> "1011".chars();
                    case 'C' -> "1100".chars();
                    case 'D' -> "1101".chars();
                    case 'E' -> "1110".chars();
                    case 'F' -> "1111".chars();
                    default -> IntStream.empty();
                })
                .mapToObj(c -> c - '0')
                .toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day16.txt"));
        List<Integer> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
