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
import java.util.stream.LongStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.Utils.nextBooleans;
import static ro.derbederos.untwist.Utils.nextFloats;

public class FreePascalRandomTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new FreePascalRandom(123456789013L);
    }

    @Override
    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        super.testSet32BitSeedIntVsLongVsArray();
    }

    @Override
    @Test
    public void testSet64bitSeedLongVsArray() {
        final long seedLong = 0x1234567823456789L;
        final int[] seedArray = {0x23456789, 0x12345678};

        ReversibleMersenneTwister rLong = makeGenerator();
        rLong.setSeed(seedLong);
        int[] actualLong = nextInts(rLong, 10, 0, 1000).toArray();

        ReversibleMersenneTwister rArray = makeGenerator();
        rArray.setSeed(seedArray);
        int[] actualArray = nextInts(rArray, 10, 0, 1000).toArray();

        assertThat("LongVsArray", actualLong, equalTo(actualArray));
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));

        nextInt = generator.nextInt(0);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    public void testPrevIntIAE2() {
        int prevInt = generator.prevInt(-1);
        assertThat(prevInt, equalTo(0));

        prevInt = generator.prevInt(0);
        assertThat(prevInt, equalTo(0));
    }

    @Override
    @Test
    public void testNextIntNeg() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    @Test
    public void testPrevIntNeg() {
        int prevInt = generator.prevInt(-1);
        assertThat(prevInt, equalTo(0));
    }

    @Override
    @Test
    public void testNextLongNeg() {
        long nextLong = generator.nextLong(-16);
        assertThat(nextLong, equalTo(9L));
    }

    @Override
    @Test
    public void testPrevLongNeg() {
        long prevLong = generator.prevLong(-16);
        assertThat(prevLong, equalTo(1L));
    }

    @Test
    public void testNextLong_0() {
        boolean result = LongStream.generate(() -> generator.nextLong(0))
                .limit(1000)
                .allMatch(value -> value == 0);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testPrevLong_0() {
        boolean result = LongStream.generate(() -> generator.prevLong(0))
                .limit(1000)
                .allMatch(value -> value == 0);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextInt_NegativeExactValue() {
        int[] expected = {-8, -15, -8, -4, -5, -14, -3, -15, -6, -10,
                -10, -3, -7, -8, -7, -10, -13, -5, -1, -11,
                -6, -14, -15, -15, -9, -12, -4, -8};
        int[] actual = IntStream.generate(() -> generator.nextInt(-16)).limit(expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_NegativeWideRangeExactValue() {
        int[] expected = {-922588439, 1115495385, 1463004829, -877651315, -552043731, 858164198, 577624126,
                1031595005, 574779647, -1103584447, 1201729266, 211323636, -954599819, 848277427,
                -659487656, -700392355, 118555058, -238923432, -632969808, -1443725123, 990536006,
                483966470, -1084035551, -802702746, -92753265, 564258807, 788455403, 289890560};
        int[] actual = nextInts(generator, expected.length, 1_500_000_000, -1_500_000_000).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {7, 15, 8, 3, 4, 14, 2, 15, 5, 9};
        int[] actual = IntStream.generate(() -> generator.nextInt(16)).limit(expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {2131728873, -149450095, -2087059751, 1068585415, 1209760669,
                -425486438, 783461773, -80805226, 1545398317, -1623044361};
        int[] actual = nextInts(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntWideRangeExactValue() {
        int[] expected = {-219591959, 1777661472, 1811113462, 397238862, 1327765389, 2087358183, -766758486,
                580066454, 276905054, -845472133, -498169491, 558293928, -89985366, 178091724,
                -490444948, -184714879, -796294707, 1327886319, 382992891, 1146587248};
        int[] actual = nextInts(generator, expected.length, -1_000_000_000, Integer.MAX_VALUE).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {9L, 9L, 13L, 13L, 13L, 6L, 14L, 13L, 15L, 1L};
        long[] actual = nextLongs(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-641883268277364247L, 4589539412615495385L, -1827450334891770979L, -347055802232427123L,
                -6970922448906819539L, 2488676750358164198L, -8896639325777151682L, -6782370575323180803L,
                5196967370074779647L, -5701509883458360255L};
        long[] actual = nextLongs(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.4963318055961281, 0.965203438187018, 0.5140685348305851, 0.2487994299735874,
                0.2816693552304059, 0.9009337187744677, 0.1824139088857919, 0.9811860672198236,
                0.35981608484871686, 0.6221055367495865};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        Float[] expected = {0.4963318F, 0.96520346F, 0.51406854F, 0.24879943F, 0.28166935F,
                0.90093374F, 0.1824139F, 0.9811861F, 0.35981607F, 0.62210554F};
        Float[] actual = nextFloats(generator, expected.length).toArray(Float[]::new);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        Boolean[] expected = {false, true, true, false, false, true, false, true, false, true,
                true, false, false, true, false, true, true, false, false, true};
        Boolean[] actual = nextBooleans(generator, expected.length).toArray(Boolean[]::new);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testPrevLong() {
        long expected = generator.nextLong(0x7ABCDEF8FFFFFFFFL);
        long actual = generator.prevLong(0x7ABCDEF8FFFFFFFFL);

        assertThat(expected, equalTo(8581488768577411561L));
        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCalls() {
        super.testNextPrevMixedCalls();
    }
}
