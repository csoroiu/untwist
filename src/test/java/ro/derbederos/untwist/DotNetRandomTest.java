/*
 * Copyright (c) 2017-2018 Claudiu Soroiu
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

import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.fail;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.Utils.nextBooleans;
import static ro.derbederos.untwist.Utils.nextFloats;

public class DotNetRandomTest extends ReverseRandomGeneratorAbstractTest<DotNetRandom> {

    @Override
    protected DotNetRandom makeGenerator() {
        return new DotNetRandom(-0x3f97396e);
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        try {
            generator.nextInt(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
        int nextInt = generator.nextInt(0);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    @Test
    public void testPrevIntIAE2() {
        try {
            generator.prevInt(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }

        int prevInt = generator.prevInt(0);
        assertThat(prevInt, equalTo(0));
    }

    @Override
    @Test
    @Ignore
    public void testNextInt2() {
        super.testNextInt2();
    }

    @Test
    public void testNextIntIsPositiveNumber() {
        boolean result = RandomUtils.nextInts(generator, 429_496_730)
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

    @Override
    @Test
    public void testNextIntWideRangeExactValue() {
        int[] expected = {72659689, 479570516, 1964597322, 1152315378, 835159700, 1400239888, 824890196,
                -84625816, 934767641, 1787676899, -950803732, -984901899, 1160627160, 854462981,
                -375650433, 751425218, -869369328, -311654904, 1868174591, 1892969089};
        int[] actual = nextInts(generator, expected.length, -1_000_000_000, Integer.MAX_VALUE).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {5, 10, 0, 10, 14, 0, 5, 1, 2, 4};
        int[] actual = nextInts(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {683762528, 1373746042, 128503505, 1360364556, 1897922134,
                90664323, 789505134, 235596161, 356723453, 608884583};
        int[] actual = nextInts(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {1L, 3L, 13L, 9L, 14L, 11L, 15L, 14L, 4L, 10L};
        long[] actual = nextLongs(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {7160971568492367569L, -8931728783750763133L, -1986638387611489539L, -2925306645117407511L,
                6258300555690268542L, -554966206143487861L, 3824004281963357759L, -2002195686033151106L,
                -777744680147937180L, 7928930161609697530L};
        long[] actual = nextLongs(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.31840173914954145, 0.63970035064951536, 0.059839107589721267, 0.63346911064976319,
                0.88378886453983785, 0.042218865380724363, 0.36764197720570579, 0.10970801166710817,
                0.16611230241419389, 0.28353397887364679};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        Float[] expected = {0.31840175F, 0.63970035F, 0.059839107F, 0.6334691F, 0.8837889F,
                0.042218864F, 0.367642F, 0.10970801F, 0.1661123F, 0.283534F};
        Float[] actual = nextFloats(generator, expected.length).toArray(Float[]::new);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        Boolean[] expected = {false, true, false, true, true, false, false, false, false, false,
                true, false, false, true, false, true, false, true, true, false};
        Boolean[] actual = nextBooleans(generator, expected.length).toArray(Boolean[]::new);

        assertThat(actual, equalTo(expected));
    }

    // -----------
    // STATE TESTS
    // -----------
    private static final int COMPARE_STEPS = 100_000;

    @Test
    public void testStateNextPrev() {
        byte[] expected = generator.getState();

        //going forward
        generator.nextInt();
        //going back to the initial state
        generator.prevInt();

        byte[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.nextInt()) {
            byte[] expected = generator.getState();

            //going forward
            generator.nextInt();
            //going back to the initial state
            generator.prevInt();

            byte[] actual = generator.getState();

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
        byte[] expected = generator.getState();

        //going back
        generator.prevInt();
        //going forward to the initial state
        generator.nextInt();

        byte[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.prevInt()) {
            byte[] expected = generator.getState();

            //going back
            generator.prevInt();
            //going forward to the initial state
            generator.nextInt();

            byte[] actual = generator.getState();

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
        byte[] expected = dotNetRandom1.getState();


        DotNetRandom dotNetRandom2 = new DotNetRandom(-0x3f97396e);
        dotNetRandom2.prevInt();
        dotNetRandom2.nextInt();
        byte[] actual = dotNetRandom2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
