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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.Utils.nextBooleans;

@RunWith(DataProviderRunner.class)
public class MersenneTwisterPy3kTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new MersenneTwisterPy3k(123456789013L);
    }

    @Test
    public void testSetSeedArray() {
        int[] largeseed = new int[625];
        Arrays.fill(largeseed, 0x01020304);
        largeseed[0] = 0x01020305;

        MersenneTwisterPy3k generator = new MersenneTwisterPy3k(largeseed);
        int[] expected = {208, 832, 482, 259, 706, 457, 453, 472, 266, 84};
        int[] actual = nextInts(generator, expected.length, 0, 1000).toArray();

        assertThat(actual, equalTo(expected));
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
    public void testNextInt16ExactValue() {
        int[] expected = {15, 7, 9, 5, 11, 4, 13, 14, 9, 1};
        int[] actual = nextInts(generator, expected.length, 0, 16).toArray();

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
        int[] expected = {2131728873, -149450095, 1068585415, 1209760669, -425486438, 783461773, -80805226,
                1545398317, 579440209, 1816672574, 1926025469, -761630721, 1210013257, 152431681,
                1534102514, -451857811, -226991116, -92257937, -949255933, 997148851};
        int[] actual = nextInts(generator, expected.length, -1_000_000_000, Integer.MAX_VALUE).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {15L, 7L, 9L, 5L, 11L, 4L, 13L, 14L, 9L, 1L};
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
        double[] expected = {0.4963318106919783, 0.5140685308635192, 0.2816693551907965, 0.18241391316939937,
                0.35981608645696583, 0.632311993352409, 0.4229770415850893, 0.44843774760013033,
                0.8226690238842976, 0.03549077131539968};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        double[] expected = {0.4963318109512329, 0.5140685439109802, 0.28166934847831726, 0.18241390585899353,
                0.3598160743713379, 0.632311999797821, 0.4229770302772522, 0.44843775033950806,
                0.8226690292358398, 0.035490769892930984};
        double[] actual = DoubleStream.generate(generator::nextFloat).limit(expected.length).toArray();

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

    @Test
    public void testNextLongVsIntRangeIntMaxValue() {
        ReversibleMersenneTwister generator1 = makeGenerator();
        ReversibleMersenneTwister generator2 = makeGenerator();
        long[] expected = nextLongs(generator2, 700, 0, Integer.MAX_VALUE).toArray();
        long[] actual = LongStream.generate(() -> generator1.nextInt(Integer.MAX_VALUE)).limit(700).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLongVsByteArray() {
        ReversibleMersenneTwister generator1 = makeGenerator();
        ReversibleMersenneTwister generator2 = makeGenerator();
        long[] expected = nextLongs(generator1, 700).toArray();

        byte[] b = new byte[8];
        long[] actual = LongStream.generate(() -> {
            generator2.nextBytes(b);
            return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
        }).limit(700).toArray();

        assertThat(actual, equalTo(expected));
    }

    @DataProvider(format = "%m %i")
    public static Object[][] dataProviderTestNextBytes() {
        //@formatter:off
        return new Object[][]{
                {new byte[]{-23, -103, 15, 127, -111, -110, 23, -9}},
                {new byte[]{-23, -103, 15, 127, -110, 23, -9}},
                {new byte[]{-23, -103, 15, 127, 23, -9}},
                {new byte[]{-23, -103, 15, 127, -9}},
        };
        //@formatter:on
    }

    @Test
    @UseDataProvider("dataProviderTestNextBytes")
    public void testNextBytes(byte[] expected) {
        byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        assertThat(actual, equalTo(expected));
    }

    @DataProvider(format = "%m %i")
    public static Object[][] dataProviderTestNextBytesRange() {
        //@formatter:off
        return new Object[][]{
                {new byte[]{0, 0, -23, -103, 15, 127, -111, -110, 23, -9, 0}},
                {new byte[]{0, 0, -23, -103, 15, 127, -110, 23, -9, 0}},
                {new byte[]{0, 0, -23, -103, 15, 127, 23, -9, 0}},
                {new byte[]{0, 0, -23, -103, 15, 127, -9, 0}},
        };
        //@formatter:on
    }

    @Test
    @UseDataProvider("dataProviderTestNextBytesRange")
    public void testNextBytesRange(byte[] expected) {
        byte[] actual = new byte[expected.length];
        generator.nextBytes(actual, 2, expected.length - 3);
        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCalls() {
        super.testNextPrevMixedCalls();
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCallsNoGaussian() {
        super.testNextPrevMixedCallsNoGaussian();
    }

    @Override
    @Test
    public void testPrevLong() {
        long expected = generator.nextLong(0x7ABCDEF8FFFFFFFFL);
        long actual = generator.prevLong(0x7ABCDEF8FFFFFFFFL);

        assertThat(expected, equalTo(2294769705264217817L));
        assertThat(actual, equalTo(expected));
    }
}
