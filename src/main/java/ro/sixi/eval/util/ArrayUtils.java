package ro.sixi.eval.util;

import java.util.Random;

public class ArrayUtils {

    private static int[] getPermutation(int n, int steps, Random randomizer) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        for (int k = 0; k < steps; k++) {
            for (int i = 0; i < n; i++) {
                int j = randomizer.nextInt(n);
                if (i == j)
                    continue;
                perm[i] ^= perm[j];
                perm[j] ^= perm[i];
                perm[i] ^= perm[j];
            }
        }
        return perm;
    }

    public static int[] getPermutation(int n, Random randomizer) {
        // http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        return getPermutation(n, 1, randomizer);
    }

    public static int[] getPermutationInsideOut(int n, Random randomizer) {
        // http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        int[] perm = new int[n];
        for (int i = 1; i < n; i++) {
            int j = randomizer.nextInt(i + 1);
            perm[i] = perm[j];
            perm[j] = i;
        }
        return perm;
    }

    public static void rotateLeft(int[] v, int from, int to) {
        int tmp = v[from];
        int i;
        for (i = from + 1; i < to; i++) {
            v[i - 1] = v[i];
        }
        v[i - 1] = tmp;
    }

    public static void rotate(int[] v, int from, int to) {
        int tmp = v[to - 1];
        int i;
        for (i = to - 1; i > from; i--) {
            v[i] = v[i - 1];
        }
        v[i] = tmp;
    }
}
