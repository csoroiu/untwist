package ro.derbederos.untwist;

import org.junit.Test;

import java.util.Random;

import static ro.derbederos.untwist.ArrayUtils.getTree;

public class ArrayUtilsTest {

    @Test
    public void testRandomTree() {
        Random r = new Random(123456789013L);
        int maxDepth = 0;
        for (int i = 0; i < 1000; i++) {
            int[] tree = getTree(10000, r);
            maxDepth = Math.max(maxDepth, assertTree(tree));
        }
        System.out.println("maxDepth = " + maxDepth);
    }

    private static int assertTree(int[] tree) {
        int maxDepth = 0;
        for (int i = 0; i < tree.length; i++) {
            int c = 0;
            int j = i;
            while (c < tree.length && j != -1) {
                j = tree[j];
                c++;
            }
            if (c >= tree.length) {
                throw new AssertionError("not a tree");
            }
            maxDepth = Math.max(maxDepth, c);
        }
        return maxDepth;
    }

}
