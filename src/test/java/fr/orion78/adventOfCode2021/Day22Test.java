package fr.orion78.adventOfCode2021;

import fr.orion78.adventOfCode2021.Day22.Leaf;
import fr.orion78.adventOfCode2021.Day22.Node;
import fr.orion78.adventOfCode2021.Day22.Step;
import fr.orion78.adventOfCode2021.Day22.Tree;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class Day22Test {
    @Test
    public void testCount() {
        Leaf l = new Leaf(10, 20, 10, 20, 10, 20, true);

        Assert.assertEquals(l.countLit(), 1_000);

        Leaf l2 = new Leaf(30, 40, 30, 40, 30, 40, true);
        Tree t = new Tree(10, 40, 10, 40, 10, 40, List.of(l, l2));

        Assert.assertEquals(t.countLit(), 2_000);
    }

    @Test
    public void testNoChangeWhenSameLighting() {
        Leaf l = new Leaf(10, 20, 10, 20, 10, 20, true);

        Node res = l.step(new Step(true, -50, 50, -50, 50, -50, 50));

        Assert.assertSame(l, res);
    }

    @Test
    public void testNoIntersection() {
        Leaf l = new Leaf(10, 20, 10, 20, 10, 20, true);

        Node res = l.step(new Step(false, 20, 30, 20, 30, 20, 30));
        Assert.assertSame(l, res);

        res = l.step(new Step(false, 0, 10, 0, 10, 0, 10));
        Assert.assertSame(l, res);
    }

    @Test
    public void testFullyEmbedded() {
        Leaf l = new Leaf(10, 20, 10, 20, 10, 20, true);

        Node res = l.step(new Step(false, -50, 50, -50, 50, -50, 50));

        Assert.assertTrue(res instanceof Leaf);
        Assert.assertFalse(((Leaf) res).lit);
        Assert.assertEquals(res.xMin, l.xMin);
        Assert.assertEquals(res.xMax, l.xMax);
        Assert.assertEquals(res.yMin, l.yMin);
        Assert.assertEquals(res.yMax, l.yMax);
        Assert.assertEquals(res.zMin, l.zMin);
        Assert.assertEquals(res.zMax, l.zMax);

        // Max touching
        Node res2 = l.step(new Step(false, -50, 20, -50, 20, -50, 20));

        Assert.assertTrue(res2 instanceof Leaf);
        Assert.assertFalse(((Leaf) res2).lit);
        Assert.assertEquals(res2.xMin, l.xMin);
        Assert.assertEquals(res2.xMax, l.xMax);
        Assert.assertEquals(res2.yMin, l.yMin);
        Assert.assertEquals(res2.yMax, l.yMax);
        Assert.assertEquals(res2.zMin, l.zMin);
        Assert.assertEquals(res2.zMax, l.zMax);

        // Min touching
        Node res3 = l.step(new Step(false, 10, 50, 10, 50, 10, 50));

        Assert.assertTrue(res3 instanceof Leaf);
        Assert.assertFalse(((Leaf) res3).lit);
        Assert.assertEquals(res3.xMin, l.xMin);
        Assert.assertEquals(res3.xMax, l.xMax);
        Assert.assertEquals(res3.yMin, l.yMin);
        Assert.assertEquals(res3.yMax, l.yMax);
        Assert.assertEquals(res3.zMin, l.zMin);
        Assert.assertEquals(res3.zMax, l.zMax);
    }

    @Test
    public void testStep() {
        Leaf l = new Leaf(0, 50, 0, 50, 0, 50, false);

        Node res = l.step(new Step(true, 10, 20, 10, 20, 10, 20));

        Assert.assertTrue(res instanceof Tree);
        Assert.assertEquals(res.xMin, l.xMin);
        Assert.assertEquals(res.xMax, l.xMax);
        Assert.assertEquals(res.yMin, l.yMin);
        Assert.assertEquals(res.yMax, l.yMax);
        Assert.assertEquals(res.zMin, l.zMin);
        Assert.assertEquals(res.zMax, l.zMax);

        Assert.assertEquals(((Tree) res).children.size(), 27);

        Assert.assertEquals(res.countLit(), 1_000);
    }

    @Test
    public void testStep2() {
        Leaf l = new Leaf(0, 50, 0, 50, 0, 50, false);

        Node res = l.step(new Step(true, 0, 10, 0, 10, 0, 10));

        Assert.assertTrue(res instanceof Tree);
        Assert.assertEquals(res.xMin, l.xMin);
        Assert.assertEquals(res.xMax, l.xMax);
        Assert.assertEquals(res.yMin, l.yMin);
        Assert.assertEquals(res.yMax, l.yMax);
        Assert.assertEquals(res.zMin, l.zMin);
        Assert.assertEquals(res.zMax, l.zMax);

        Assert.assertEquals(((Tree) res).children.size(), 8);

        Assert.assertEquals(res.countLit(), 1_000);
    }

    @Test
    public void testStep3() {
        Leaf l = new Leaf(0, 50, 0, 50, 0, 50, false);

        Node res = l.step(new Step(true, 40, 50, 40, 50, 40, 50));

        Assert.assertTrue(res instanceof Tree);
        Assert.assertEquals(res.xMin, l.xMin);
        Assert.assertEquals(res.xMax, l.xMax);
        Assert.assertEquals(res.yMin, l.yMin);
        Assert.assertEquals(res.yMax, l.yMax);
        Assert.assertEquals(res.zMin, l.zMin);
        Assert.assertEquals(res.zMax, l.zMax);

        Assert.assertEquals(((Tree) res).children.size(), 8);

        Assert.assertEquals(res.countLit(), 1_000);
    }

    @Test
    public void testStep4() {
        Leaf l = new Leaf(0, 50, 0, 50, 0, 50, false);

        Node res = l.step(new Step(true, -10, 60, 30, 40, 0, 50));

        Assert.assertTrue(res instanceof Tree);
        Assert.assertEquals(res.xMin, l.xMin);
        Assert.assertEquals(res.xMax, l.xMax);
        Assert.assertEquals(res.yMin, l.yMin);
        Assert.assertEquals(res.yMax, l.yMax);
        Assert.assertEquals(res.zMin, l.zMin);
        Assert.assertEquals(res.zMax, l.zMax);

        Assert.assertEquals(((Tree) res).children.size(), 3);

        Assert.assertEquals(res.countLit(), 10*50*50);
    }

    @Test
    public void testTwoSteps() {
        Leaf l = new Leaf(0, 50, 0, 50, 0, 50, false);

        Node res = l.step(new Step(true, 10, 40, 10, 40, 10, 40))
                .step(new Step(false, 0, 50, 20, 30, 20, 30));

        Assert.assertEquals(res.countLit(), 30*30*30-30*10*10);

    }
}
