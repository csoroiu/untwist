/*
 * Copyright (c) 2017 Claudiu Soroiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.*;

public class ArrayUtils {

    @Deprecated
    public static int[] getPermutationLegacy(int n, RandomGenerator randomizer) {
        int[] perm = getIdentityPermutation(n);
        for (int i = 0; i < n; i++) {
            swap(perm, i, randomizer.nextInt(n));
        }
        return perm;
    }

    public static int[] getTree(int n, RandomGenerator randomizer) {
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

    private static int[] getIdentityPermutation(int n) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        return perm;
    }

    public static int[] getPermutation(int n, RandomGenerator randomizer) {
        int[] perm = getIdentityPermutation(n);
        shuffle(perm, randomizer);
        return perm;
    }

    public static void shuffle(int[] source, RandomGenerator randomizer) {
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
        for (int i = source.length; i > 1; i--) {
            swap(source, i - 1, randomizer.nextInt(i));
        }
    }

    public static int[] getPermutationInsideOut(int n, RandomGenerator randomizer) {
        int[] perm = getIdentityPermutation(n);
        shuffleInsideOut(perm, randomizer);
        return perm;
    }

    public static void shuffleInsideOut(int[] source, RandomGenerator randomizer) {
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_.22inside-out.22_algorithm
        for (int i = 0; i < source.length; i++) {
            swap(source, i, randomizer.nextInt(i + 1));
        }
    }

    private static void swap(int[] source, int i, int j) {
        int tmp = source[i];
        source[i] = source[j];
        source[j] = tmp;
    }

    public static void rotateLeft(int[] v, int from, int to) {
        int tmp = v[from];
        int i;
        for (i = from + 1; i < to; i++) {
            v[i - 1] = v[i];
        }
        v[i - 1] = tmp;
    }

    public static void rotateRight(int[] v, int from, int to) {
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

    public static float[] generateFloatArray(int size, DoubleSupplier supplier) {
        Objects.requireNonNull(supplier);
        float[] result = new float[size];
        for (int i = 0; i < result.length; i++) {
            result[i] = (float) supplier.getAsDouble();
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
