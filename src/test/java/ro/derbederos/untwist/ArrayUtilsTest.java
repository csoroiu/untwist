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

import org.apache.commons.math3.random.RandomAdaptor;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class ArrayUtilsTest {

    RandomGenerator randomizer;

    @Before
    public void setUp() throws Exception {
        randomizer = new MersenneTwisterPy3k(new int[]{0x123, 0x234, 0x345, 0x456});
    }

    @Test
    public void testRandomTree() {
        ReversibleJdkRandom randomizer = new ReversibleJdkRandom();
        int maxDepth = 0;
        int N = 10000;
        for (int i = 0; i < 1000; i++) {
            int[] tree = getTree(N, randomizer);
            maxDepth = Math.max(maxDepth, assertTree(tree));
        }
        //seed = 270579156758789, depth = 30
        System.out.println("seed = " + randomizer.getSeed());
        System.out.println("maxDepth = " + maxDepth);
        System.out.println("theoretical longest path (4.311 log n) = " + (4.311 * Math.log(N)));
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

    @Test
    public void testGetPermutationLegacy() {
        int[] expected = new int[]{1, 2, 7, 4, 6, 8, 5, 0, 9, 3};
        int[] actual = getPermutationLegacy(10, randomizer);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testGetPermutationModern() {
        int[] expected = new int[]{5, 2, 8, 4, 7, 0, 6, 1, 9, 3};
        int[] actual = getPermutation(10, randomizer);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testShuffleModern() {
        int[] expected = new int[]{5, 2, 8, 4, 7, 0, 6, 1, 9, 3};
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffle(actual, randomizer);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testShuffleModernVsCollections() {
        RandomGenerator randomizer1 = new MersenneTwisterPy3k(new int[]{0x123, 0x234, 0x345, 0x456});
        RandomGenerator randomizer2 = new MersenneTwisterPy3k(new int[]{0x123, 0x234, 0x345, 0x456});

        Integer[] expected = new Integer[]{5, 2, 8, 4, 7, 0, 6, 1, 9, 3};
        int[] actualInt = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<Integer> actualInteger = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        shuffle(actualInt, randomizer1);
        Collections.shuffle(actualInteger, new RandomAdaptor(randomizer2));

        assertThat(IntStream.of(actualInt).boxed().toArray(), equalTo(actualInteger.toArray(new Integer[actualInt.length])));
        assertThat(IntStream.of(actualInt).boxed().toArray(), equalTo(expected));
    }

    @Test
    public void testShuffleModernTwoCalls() {
        int[] expected = new int[]{2, 9, 3, 5, 4, 1, 8, 0, 7, 6};
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffle(actual, randomizer);
        shuffle(actual, randomizer);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testShuffleModernVsCollectionsTwoCalls() {
        RandomGenerator randomizer1 = new MersenneTwisterPy3k(new int[]{0x123, 0x234, 0x345, 0x456});
        RandomGenerator randomizer2 = new MersenneTwisterPy3k(new int[]{0x123, 0x234, 0x345, 0x456});

        Integer[] expected = new Integer[]{2, 9, 3, 5, 4, 1, 8, 0, 7, 6};
        int[] actualInt = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<Integer> actualInteger = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        shuffle(actualInt, randomizer1);
        shuffle(actualInt, randomizer1);
        Collections.shuffle(actualInteger, new RandomAdaptor(randomizer2));
        Collections.shuffle(actualInteger, new RandomAdaptor(randomizer2));

        assertThat(IntStream.of(actualInt).boxed().toArray(), equalTo(actualInteger.toArray(new Integer[actualInt.length])));
        assertThat(IntStream.of(actualInt).boxed().toArray(), equalTo(expected));
    }

    @Test
    public void testGetPermutationInsideOut() {
        int[] expected = new int[]{3, 4, 7, 2, 9, 8, 5, 1, 0, 6};
        int[] actual = ArrayUtils.getPermutationInsideOut(10, randomizer);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testShuffleInsideOut() {
        int[] expected = new int[]{3, 4, 7, 2, 9, 8, 5, 1, 0, 6};
        int[] actual = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffleInsideOut(actual, randomizer);

        assertThat(actual, equalTo(expected));
    }
}
