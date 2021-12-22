package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.utils.Day;
import fr.orion78.adventOfCode2021.utils.InputParser;
import fr.orion78.adventOfCode2021.utils.Part1;
import fr.orion78.adventOfCode2021.utils.Part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Day
public class Day18 {
    public static abstract class Element implements Cloneable {
        public Pair parent;

        public Element(Pair parent) {
            this.parent = parent;
        }

        public abstract long getMagnitude();

        public abstract Element clone();
    }

    public static class Literal extends Element {
        public int value;

        public Literal(Pair parent, int value) {
            super(parent);
            this.value = value;
        }

        public long getMagnitude() {
            return value;
        }

        public Pair split() {
            int split = value / 2;
            return new Pair(this.parent, new Literal(null, split), new Literal(null, value - split));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Literal literal = (Literal) o;
            return value == literal.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }

        public Literal clone() {
            return new Literal(null, value);
        }
    }

    public static class Pair extends Element {
        public Element l;
        public Element r;

        public Pair(Pair parent, Element l, Element r) {
            super(parent);
            this.l = l;
            this.r = r;

            l.parent = this;
            r.parent = this;
        }

        public long getMagnitude() {
            return 3 * l.getMagnitude() + 2 * r.getMagnitude();
        }

        @Override
        public Pair clone() {
            return new Pair(null, l.clone(), r.clone());
        }

        public boolean explode() {
            return explode(0);
        }

        private boolean explode(int level) {
            if (level == 4) {
                // Both should be a literal or it would have exploded before
                Literal l = (Literal) this.l;
                Literal r = (Literal) this.r;

                Literal ll = getLeftLiteral(this);
                if (ll != null) {
                    ll.value += l.value;
                }
                Literal rl = getRightLiteral(this);
                if (rl != null) {
                    rl.value += r.value;
                }


                Literal newL = new Literal(this.parent, 0);
                if (this.parent.l == this) {
                    this.parent.l = newL;
                } else {
                    this.parent.r = newL;
                }

                return true;
            }
            if (l instanceof Pair l) {
                if (l.explode(level + 1)) {
                    return true;
                }
            }
            if (r instanceof Pair r) {
                if (r.explode(level + 1)) {
                    return true;
                }
            }

            return false;
        }

        private static Literal getLeftLiteral(Pair pair) {
            Pair cur = pair;

            while (cur.parent != null && cur.parent.l == cur) {
                cur = cur.parent;
            }

            if (cur.parent == null) {
                // Went to the top and nothing on the left
                return null;
            }

            Element e = cur.parent.l;
            while (e instanceof Pair p) {
                e = p.r;
            }

            return (Literal) e;
        }

        private static Literal getRightLiteral(Pair pair) {
            Pair cur = pair;

            while (cur.parent != null && cur.parent.r == cur) {
                cur = cur.parent;
            }

            if (cur.parent == null) {
                // Went to the top and nothing on the left
                return null;
            }

            Element e = cur.parent.r;
            while (e instanceof Pair p) {
                e = p.l;
            }

            return (Literal) e;
        }

        public boolean split() {
            if (l instanceof Literal l) {
                if (l.value >= 10) {
                    this.l = l.split();
                    return true;
                }
            } else if (l instanceof Pair l) {
                if (l.split()) {
                    return true;
                }
            }

            if (r instanceof Literal r) {
                if (r.value >= 10) {
                    this.r = r.split();
                    return true;
                }
            } else if (r instanceof Pair r) {
                if (r.split()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(l, pair.l) && Objects.equals(r, pair.r);
        }

        @Override
        public int hashCode() {
            return Objects.hash(l, r);
        }

        @Override
        public String toString() {
            return "[" + l + "," + r + ']';
        }
    }

    public static Element parse(String s) {
        Deque<Element> q = new ArrayDeque<>();
        AtomicBoolean newNumber = new AtomicBoolean(true);

        s.chars().forEach(i -> {
            if (i == ']') {
                Element r = q.removeLast();
                Element l = q.removeLast();
                q.addLast(new Pair(null, l, r));
            } else if (i >= '0' && i <= '9') {
                if (newNumber.get()) {
                    q.addLast(new Literal(null, i - '0'));
                    newNumber.set(false);
                } else {
                    Literal last = (Literal) q.getLast();
                    last.value = last.value * 10 + (i - '0');
                }
            } else if (i == ',') {
                newNumber.set(true);
            }
        });

        return q.removeLast();
    }

    @Part1
    public static long part1(List<Element> input) {
        return sum(input).getMagnitude();
    }

    public static Element sum(List<Element> input) {
        Element res = input.get(0).clone();

        for (int i = 1; i < input.size(); i++) {
            Pair newRes = new Pair(null, res, input.get(i).clone());
            while (newRes.explode() || newRes.split()) {
            }
            res = newRes;
        }
        return res;
    }

    @Part2
    public static long part2(List<Element> input) {
        long max = Long.MIN_VALUE;

        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (i == j) {
                    continue;
                }
                max = Math.max(max, part1(List.of(input.get(i), input.get(j))));
            }
        }

        return max;
    }

    @InputParser
    public static List<Element> parse(Stream<String> stream) {
        return stream.map(Day18::parse).toList();
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("day18.txt"));
        List<Element> l = parse(br.lines());

        System.out.println(part1(l));
        System.out.println(part2(l));
    }
}
