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

public class ArrayUtils {

    public static int[] getTree(RandomGenerator generator, int n) {
        // http://algs4.cs.princeton.edu/41graph/GraphGenerator.java.html
        // https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence
        int[] perm = getPermutation(generator, n);
        int[] tree = new int[n];
        tree[perm[0]] = -1;
        for (int i = 1; i < n; i++) {
            int value = generator.nextInt(i);
            tree[perm[i]] = perm[value];
        }
        return tree;
    }

    public static int[] getIdentityPermutation(int n) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) {
            perm[i] = i;
        }
        return perm;
    }

    public static int[] getPermutation(RandomGenerator generator, int n) {
        int[] perm = getIdentityPermutation(n);
        shuffle(generator, perm);
        return perm;
    }

    public static void shuffle(RandomGenerator generator, int[] source) {
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
        for (int i = source.length; i > 1; i--) {
            swap(source, i - 1, generator.nextInt(i));
        }
    }

    public static int[] getPermutationInsideOut(RandomGenerator generator, int n) {
        int[] perm = getIdentityPermutation(n);
        shuffleInsideOut(generator, perm);
        return perm;
    }

    public static void shuffleInsideOut(RandomGenerator generator, int[] source) {
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_.22inside-out.22_algorithm
        for (int i = 0; i < source.length; i++) {
            swap(source, i, generator.nextInt(i + 1));
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
}
