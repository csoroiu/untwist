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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.nextBooleans;
import static ro.derbederos.untwist.Utils.nextFloats;
import static ro.derbederos.untwist.Utils.reverseArray;

public class DotNetRandomTest extends ReverseRandomGeneratorAbstractTest<DotNetRandom> {

    @Override
    protected DotNetRandom makeGenerator() {
        return new DotNetRandom(-0x3f97396e);
    }

    @Override
    @Test
    @Ignore
    public void testNextInt2() {
        super.testNextInt2();
    }

    @Override
    @Test
    @Ignore
    public void testNextIntWideRange() {
        super.testNextIntWideRange();
    }

    @Test
    public void testNextIntWideRangeDotNet() {
        int lower = -0x6543210F;
        int upper = 0x456789AB;
        IntSummaryStatistics statistics = IntStream
                .generate(() -> generator.nextInt(lower, upper + 1))
                .limit(1_000_000)
                .summaryStatistics();
        assertThat(statistics.getMin(), greaterThanOrEqualTo(lower));
        assertThat(statistics.getMax(), lessThanOrEqualTo(upper));
        double ratio = ((double) statistics.getMax() - (double) statistics.getMin()) /
                (((double) upper) - ((double) lower));
        Assert.assertTrue(ratio > 0.99999);
    }

    @Test
    public void testNextIntIsPositiveNumber() {
        boolean result = IntStream
                .generate(generator::nextInt)
                .limit(429_496_730)
                .allMatch((i) -> 0 <= i && i < Integer.MAX_VALUE);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(200, 100);
    }

    @Test
    public void testPrevInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        generator.prevInt(200, 100);
    }

    @Test
    public void testNextInt() {
        boolean result = IntStream.
                generate(() -> generator.nextInt(1000))
                .limit((long) 100000)
                .allMatch((i) -> 0 <= i && i < 1000);

        assertThat(result, equalTo(true));
    }

    private static IntStream nextIntsDotNet(long streamSize, int origin, int bound, DotNetRandom generator) {
        return IntStream.generate(() -> generator.nextInt(origin, bound)).limit(streamSize);
    }

    private static IntStream prevIntsDotNet(long streamSize, int origin, int bound, DotNetRandom generator) {
        return IntStream.generate(() -> generator.prevInt(origin, bound)).limit(streamSize);
    }

    @Test
    public void testNextPrevIntRange() {
        int[] expected = nextIntsDotNet(2459, -1000, 3000, generator).toArray();
        int[] actual = prevIntsDotNet(2459, -1000, 3000, generator).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextIntsDotNet(2467, -1000, 5000, generator).toArray();
        actual = prevIntsDotNet(2467, -1000, 5000, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntRange() {
        int[] expected = prevIntsDotNet(2459, -1000, 3000, generator).toArray();
        int[] actual = nextIntsDotNet(2459, -1000, 3000, generator).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevIntsDotNet(2467, -1000, 5000, generator).toArray();
        actual = nextIntsDotNet(2467, -1000, 5000, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextIntWideRangeExactValue() {
        int[] expected = {72659689, 479570516, 1964597322, 1152315378, 835159700, 1400239888, 824890196, -84625816,
                934767641, 1787676899, -950803732, -984901899, 1160627160, 854462981, -375650433, 751425218,
                -869369328, -311654904, 1868174591, 1892969089};
        int[] actual = nextIntsDotNet(expected.length, -1_000_000_000, Integer.MAX_VALUE, generator).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {5, 10, 0, 10, 14, 0, 5, 1, 2, 4};
        int[] actual = nextInts(expected.length, 0, 16, generator).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {683762528, 1373746042, 128503505, 1360364556, 1897922134,
                90664323, 789505134, 235596161, 356723453, 608884583};
        int[] actual = nextInts(expected.length, generator).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {7160971568492367569L, -8931728783750763133L, -1986638387611489539L, -2925306645117407511L,
                6258300555690268542L, -554966206143487861L, 3824004281963357759L, -2002195686033151106L,
                -777744680147937180L, 7928930161609697530L};
        long[] actual = nextLongs(expected.length, generator).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.31840173914954145, 0.6397003506495154, 0.05983910758972127, 0.6334691106497632,
                0.8837888645398378, 0.04221886538072436, 0.3676419772057058, 0.10970801166710817,
                0.1661123024141939, 0.2835339788736468};
        double[] actual = nextDoubles(expected.length, generator).toArray();

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.31840175F, 0.63970035F, 0.059839107F, 0.6334691F, 0.8837889F,
                0.042218864F, 0.367642F, 0.10970801F, 0.1661123F, 0.283534F};
        float[] actual = nextFloats(expected.length, generator);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {false, true, false, true, true, false, false, false, false, false,
                true, false, false, true, false, true, false, true, true, false};
        boolean[] actual = nextBooleans(expected.length, generator);

        assertThat(actual, equalTo(expected));
    }

    // -----------
    // STATE TESTS
    // -----------
    private static final int COMPARE_STEPS = 100_000;

    @Test
    public void testStateNextPrev() {
        int[] expected = generator.getState();

        //going forward
        generator.nextInt();
        //going back to the initial state
        generator.prevInt();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.nextInt()) {
            int[] expected = generator.getState();

            //going forward
            generator.nextInt();
            //going back to the initial state
            generator.prevInt();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNext() {
        int[] expected = generator.getState();

        //going back
        generator.prevInt();
        //going forward to the initial state
        generator.nextInt();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.prevInt()) {
            int[] expected = generator.getState();

            //going back
            generator.prevInt();
            //going forward to the initial state
            generator.nextInt();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextVsNextPrev() {
        DotNetRandom dotNetRandom1 = new DotNetRandom(-0x3f97396e);
        dotNetRandom1.prevInt();
        dotNetRandom1.nextInt();
        int[] expected = dotNetRandom1.getState();


        DotNetRandom dotNetRandom2 = new DotNetRandom(-0x3f97396e);
        dotNetRandom2.prevInt();
        dotNetRandom2.nextInt();
        int[] actual = dotNetRandom2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
