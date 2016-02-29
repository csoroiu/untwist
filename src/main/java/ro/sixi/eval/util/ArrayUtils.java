package ro.sixi.eval.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

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

    public static double[] generateDoubleArray(int limit, Supplier<Double> supplier) {
        Objects.requireNonNull(supplier);
        return generateDoubleArray(limit, (i) -> supplier.get().doubleValue());
    }

    public static double[] generateDoubleArray(int limit, IntToDoubleFunction generator) {
        double[] result = new double[limit];
        Arrays.setAll(result, generator);
        return result;
    }

    public static float[] generateFloatArray(int limit, Supplier<Float> supplier) {
        Objects.requireNonNull(supplier);
        float[] result = new float[limit];
        for (int i = 0; i < result.length; i++) {
            result[i] = supplier.get().floatValue();
        }
        return result;
    }

    public static long[] generateLongArray(int limit, Supplier<Long> supplier) {
        Objects.requireNonNull(supplier);
        return generateLongArray(limit, (i) -> supplier.get().longValue());
    }

    public static long[] generateLongArray(int limit, IntToLongFunction generator) {
        long[] result = new long[limit];
        Arrays.setAll(result, generator);
        return result;
    }

    public static int[] generateIntArray(int limit, Supplier<Integer> supplier) {
        Objects.requireNonNull(supplier);
        return generateIntArray(limit, (i) -> supplier.get().intValue());
    }

    public static int[] generateIntArray(int limit, IntUnaryOperator generator) {
        int[] result = new int[limit];
        Arrays.setAll(result, generator);
        return result;
    }

    public static boolean[] generateBooleanArray(int limit, Supplier<Boolean> supplier) {
        Objects.requireNonNull(supplier);
        boolean[] result = new boolean[limit];
        for (int i = 0; i < result.length; i++) {
            result[i] = supplier.get().booleanValue();
        }
        return result;
    }

    public static <A> A[] generateArray(IntFunction<A[]> ctor, int limit, Supplier<A> supplier) {
        Objects.requireNonNull(supplier);
        return generateArray(ctor, limit, (i) -> supplier.get());
    }

    public static <A> A[] generateArray(IntFunction<A[]> ctor, int limit, IntFunction<A> generator) {
        Objects.requireNonNull(ctor);
        A[] result = ctor.apply(limit);
        Arrays.setAll(result, generator);
        return result;
    }
}
