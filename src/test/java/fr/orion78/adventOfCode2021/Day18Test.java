package fr.orion78.adventOfCode2021;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Stream;

public class Day18Test {
    @DataProvider
    public static Object[][] magnitudesExamples() {
        return new Object[][]{
                new Object[]{"[[1,2],[[3,4],5]]", 143},
                new Object[]{"[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 1384},
                new Object[]{"[[[[1,1],[2,2]],[3,3]],[4,4]]", 445},
                new Object[]{"[[[[3,0],[5,3]],[4,4]],[5,5]]", 791},
                new Object[]{"[[[[5,0],[7,4]],[5,5]],[6,6]]", 1137},
                new Object[]{"[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", 3488},
                new Object[]{"[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]", 4140},
                new Object[]{"[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]", 3993}
        };
    }

    @Test(dataProvider = "magnitudesExamples")
    public void testMagnitudes(String input, long expectedResult) {
        Day18.Element e = Day18.parse(input);
        Assert.assertEquals(e.getMagnitude(), expectedResult);
    }

    @DataProvider
    public static Object[][] explosionExamples() {
        return new Object[][]{
                new Object[]{"[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"},
                new Object[]{"[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]"},
                new Object[]{"[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"},
                new Object[]{"[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"},
                new Object[]{"[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"},

                new Object[]{"[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"},
                new Object[]{"[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", "[[[[0,7],4],[15,[0,13]]],[1,1]]"},
                new Object[]{"[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"},
        };
    }

    @Test(dataProvider = "explosionExamples")
    public void testExplosions(String input, String expectedResult) {
        Day18.Element e = Day18.parse(input);

        boolean explode = ((Day18.Pair) e).explode();
        Assert.assertTrue(explode);

        Assert.assertEquals(e, Day18.parse(expectedResult));
    }

    @DataProvider
    public static Object[][] splitExamples() {
        return new Object[][]{
                new Object[]{"[[[[0,7],4],[15,[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"},
                new Object[]{"[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"},
        };
    }

    @Test(dataProvider = "splitExamples")
    public void testSplit(String input, String expectedResult) {
        Day18.Element e = Day18.parse(input);

        boolean explode = ((Day18.Pair) e).explode();
        Assert.assertFalse(explode);

        boolean split = ((Day18.Pair) e).split();
        Assert.assertTrue(split);

        Assert.assertEquals(e, Day18.parse(expectedResult));
    }

    @DataProvider
    public static Object[][] sumExamples() {
        return new Object[][]{
                new Object[]{Stream.of("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]"), "[[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"},

                new Object[]{Stream.of("[1,1]", "[2,2]", "[3,3]", "[4,4]"), "[[[[1,1],[2,2]],[3,3]],[4,4]]"},
                new Object[]{Stream.of("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]"), "[[[[3,0],[5,3]],[4,4]],[5,5]]"},
                new Object[]{Stream.of("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]"), "[[[[5,0],[7,4]],[5,5]],[6,6]]"},

                new Object[]{
                        Stream.of("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
                                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                                "[7,[5,[[3,8],[1,4]]]]",
                                "[[2,[2,2]],[8,[8,1]]]",
                                "[2,9]",
                                "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                                "[[[5,[7,4]],7],1]",
                                "[[[[4,2],2],6],[8,7]]"),
                        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"},
                new Object[]{
                        Stream.of("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                                "[[[5,[2,8]],4],[5,[[9,9],0]]]",
                                "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
                                "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
                                "[[[[5,4],[7,7]],8],[[8,3],8]]",
                                "[[9,3],[[9,9],[6,[4,9]]]]",
                                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
                                "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"),
                        "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"},

                new Object[]{
                        Stream.of("[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
                                "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]"),
                        "[[[[7,8],[6,6]],[[6,0],[7,7]]],[[[7,8],[8,8]],[[7,9],[0,6]]]]"},
        };
    }

    @Test(dataProvider = "sumExamples")
    public void testSum(Stream<String> input, String expectedResult) {
        List<Day18.Element> l = Day18.parse(input);

        Assert.assertEquals(Day18.sum(l), Day18.parse(expectedResult));
    }

    @Test
    public void testDoubleDigitParse() {
        Assert.assertEquals(Day18.parse("[[[[0,7],4],[15,[0,13]]],[1,1]]").toString(), "[[[[0,7],4],[15,[0,13]]],[1,1]]");
    }
}
