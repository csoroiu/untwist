package ro.derbederos.untwist;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.*;

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

    public static int[] getTree(int n, Random randomizer) {
        // http://algs4.cs.princeton.edu/41graph/GraphGenerator.java.html
        // https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence
        int[] perm = getPermutation(n, randomizer);
        int[] tree = new int[n];
        tree[perm[0]] = -1;
        for (int i = 1; i < n; i++) {
            int value = randomizer.nextInt(i);
            tree[perm[i]] = perm[value];
        }
        return tree;
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

    public static double[] generateDoubleArray(int size, DoubleSupplier supplier) {
        Objects.requireNonNull(supplier);
        return generateDoubleArray(size, (i) -> supplier.getAsDouble());
    }

    public static double[] generateDoubleArray(int size, IntToDoubleFunction generator) {
        double[] result = new double[size];
        Arrays.setAll(result, generator);
        return result;
    }

    public static float[] generateFloatArray(int size, Supplier<Float> supplier) {
        Objects.requireNonNull(supplier);
        float[] result = new float[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = supplier.get();
        }
        return result;
    }

    public static long[] generateLongArray(int size, LongSupplier supplier) {
        Objects.requireNonNull(supplier);
        return generateLongArray(size, (i) -> supplier.getAsLong());
    }

    public static long[] generateLongArray(int size, IntToLongFunction generator) {
        long[] result = new long[size];
        Arrays.setAll(result, generator);
        return result;
    }

    public static int[] generateIntArray(int size, IntSupplier supplier) {
        Objects.requireNonNull(supplier);
        return generateIntArray(size, (i) -> supplier.getAsInt());
    }

    public static int[] generateIntArray(int size, IntUnaryOperator generator) {
        int[] result = new int[size];
        Arrays.setAll(result, generator);
        return result;
    }

    public static boolean[] generateBooleanArray(int size, BooleanSupplier supplier) {
        Objects.requireNonNull(supplier);
        boolean[] result = new boolean[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = supplier.getAsBoolean();
        }
        return result;
    }

    public static <A> A[] generateArray(IntFunction<A[]> ctor, int size, Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return generateArray(ctor, size, (i) -> supplier.get());
    }

    public static <A> A[] generateArray(IntFunction<A[]> ctor, int size, IntFunction<A> generator) {
        Objects.requireNonNull(ctor);
        A[] result = ctor.apply(size);
        Arrays.setAll(result, generator);
        return result;
    }
}
