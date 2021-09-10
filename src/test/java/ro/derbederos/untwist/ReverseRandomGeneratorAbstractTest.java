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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGeneratorAbstractTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.IntSummaryStatistics;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static ro.derbederos.untwist.RandomUtils.*;
import static ro.derbederos.untwist.Utils.*;

@Ignore
@RunWith(DataProviderRunner.class)
public abstract class ReverseRandomGeneratorAbstractTest<T extends ReverseRandomGenerator>
        extends RandomGeneratorAbstractTest {

    /**
     * RandomGenerator under test
     */
    protected final T generator;

    /**
     * Initialize generator and randomData instance in superclass.
     */
    ReverseRandomGeneratorAbstractTest() {
        generator = makeGenerator();
        super.generator = this.generator;
        randomData = new RandomDataGenerator(generator);
    }

    /**
     * Override this method in subclasses to provide a concrete generator to test.
     * Return a generator seeded with a fixed seed.
     */
    @Override
    protected abstract T makeGenerator();

    @Override
    @Test
    public void testNextInt2() {
        long N = 10000;
        long positives = RandomUtils.nextInts(generator, N).filter(value -> value >= 0).count();
        long walk = 2 * positives - N;
        assertThat("Walked too far astray: " + walk + "\nNote: This " +
                        "test will fail randomly about 1 in 100 times.",
                Math.abs((double) walk), lessThan(Math.sqrt(N) * 2.576));
    }

    @Override
    @Test
    public void testNextLong2() {
        long N = 1000;
        long positives = RandomUtils.nextLongs(generator, N).filter(value -> value >= 0).count();
        long walk = 2 * positives - N;
        assertThat("Walked too far astray: " + walk + "\nNote: This " +
                        "test will fail randomly about 1 in 100 times.",
                Math.abs((double) walk), lessThan(Math.sqrt(N) * 2.576));
    }

    @Override
    @Test
    public void testNexBoolean2() {
        long N = 10000;
        long positives = Stream.generate(generator::nextBoolean).limit(N).filter(value -> value).count();
        long walk = 2 * positives - N;
        assertThat("Walked too far astray: " + walk + "\nNote: This " +
                        "test will fail randomly about 1 in 100 times.",
                Math.abs((double) walk), lessThan(Math.sqrt(N) * 2.576));
    }

    @Test
    @Override
    public void testNextIntWideRange() {
        int lower = -0x6543210F;
        int upper = 0x456789AB;
        IntSummaryStatistics statistics = RandomUtils.nextInts(generator, 1_000_000, lower, upper)
                .summaryStatistics();
        assertThat(statistics.getMin(), greaterThanOrEqualTo(lower));
        assertThat(statistics.getMax(), lessThanOrEqualTo(upper));
        double ratio = ((double) statistics.getMax() - (double) statistics.getMin()) /
                (((double) upper) - ((double) lower));
        assertThat(ratio, greaterThan(0.99999));
    }

//    @Test
//    @Ignore
//    public void testNextLongWideRange() {
//        long lower = -0x6543210FEDCBA987L;
//        long upper = 0x456789ABCDEF0123L;
//        LongSummaryStatistics statistics = LongStream
//                .generate(() -> generator.nextLong(lower, upper))
//                .limit(1_000_000)
//                .summaryStatistics();
//        assertThat(statistics.getMin(), greaterThanOrEqualTo(lower));
//        assertThat(statistics.getMax(), lessThanOrEqualTo(upper));
//        double ratio = ((double) statistics.getMax() - (double) statistics.getMin()) /
//                (((double) upper) - ((double) lower));
//        Assert.assertTrue(ratio > 0.99999);
//    }

    @Override
    @Test
    public void testNextIntIAE2() {
        try {
            generator.nextInt(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            generator.nextInt(0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testPrevIntIAE2() {
        try {
            generator.prevInt(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            generator.prevInt(0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    @Test
    public void testNextIntNeg() {
        assertThrows(IllegalArgumentException.class,
                     () -> generator.nextInt(-1));
    }

    @Test
    public void testPrevIntNeg() {
        assertThrows(IllegalArgumentException.class,
                     () -> generator.prevInt(-1));
    }

    @Test
    public void testNextLongNeg() {
        assertThrows(IllegalArgumentException.class,
                     () -> generator.nextLong(-16));
    }

    @Test
    public void testPrevLongNeg() {
        assertThrows(IllegalArgumentException.class,
                     () -> generator.prevLong(-16));
    }

    @Test
    public void testNextPrevInt() {
        int[] expected = nextInts(generator, 2459).toArray();
        int[] actual = prevInts(generator, 2459).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextInts(generator, 2467).toArray();
        actual = prevInts(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextInt() {
        int[] expected = prevInts(generator, 2459).toArray();
        int[] actual = nextInts(generator, 2459).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevInts(generator, 2467).toArray();
        actual = nextInts(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevIntBounded() {
        int[] expected = nextInts(generator, 2459, 0, 78209372).toArray();
        int[] actual = prevInts(generator, 2459, 0, 78209372).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextInts(generator, 2467, 0, 78209372).toArray();
        actual = prevInts(generator, 2467, 0, 78209372).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntBounded() {
        int[] expected = prevInts(generator, 2459, 0, 78209372).toArray();
        int[] actual = nextInts(generator, 2459, 0, 78209372).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevInts(generator, 2467, 0, 78209372).toArray();
        actual = nextInts(generator, 2467, 0, 78209372).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevIntRange() {
        int[] expected = nextInts(generator, 2459, -1000, 3000).toArray();
        int[] actual = prevInts(generator, 2459, -1000, 3000).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextInts(generator, 2467, -1000, 5000).toArray();
        actual = prevInts(generator, 2467, -1000, 5000).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntRange() {
        int[] expected = prevInts(generator, 2459, -1000, 3000).toArray();
        int[] actual = nextInts(generator, 2459, -1000, 3000).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevInts(generator, 2467, -1000, 5000).toArray();
        actual = nextInts(generator, 2467, -1000, 5000).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevLong() {
        long[] expected = nextLongs(generator, 2459).toArray();
        long[] actual = prevLongs(generator, 2459).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextLongs(generator, 2467).toArray();
        actual = prevLongs(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLong() {
        long[] expected = prevLongs(generator, 2459).toArray();
        long[] actual = nextLongs(generator, 2459).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevLongs(generator, 2467).toArray();
        actual = nextLongs(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevLongBounded() {
        long[] expected = nextLongs(generator, 2459, 0, 78209372).toArray();
        long[] actual = prevLongs(generator, 2459, 0, 78209372).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0L, 78209372L)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextLongs(generator, 2467, 0, 78209372).toArray();
        actual = prevLongs(generator, 2467, 0, 78209372).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLongBounded() {
        long[] expected = prevLongs(generator, 2459, 0, 78209372).toArray();
        long[] actual = nextLongs(generator, 2459, 0, 78209372).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0L, 78209372L)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevLongs(generator, 2467, 0, 78209372).toArray();
        actual = nextLongs(generator, 2467, 0, 78209372).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevLongRange() {
        long[] expected = nextLongs(generator, 2459, -1000, 3000).toArray();
        long[] actual = prevLongs(generator, 2459, -1000, 3000).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000L, 3000L)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextLongs(generator, 2467, -1000, 5000).toArray();
        actual = prevLongs(generator, 2467, -1000, 5000).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLongRange() {
        long[] expected = prevLongs(generator, 2459, -1000, 3000).toArray();
        long[] actual = nextLongs(generator, 2459, -1000, 3000).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(-1000L, 3000L)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevLongs(generator, 2467, -1000, 5000).toArray();
        actual = nextLongs(generator, 2467, -1000, 5000).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevDouble() {
        double[] expected = nextDoubles(generator, 2459).toArray();
        double[] actual = prevDoubles(generator, 2459).toArray();

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextDoubles(generator, 2467).toArray();
        actual = prevDoubles(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextDouble() {
        double[] expected = prevDoubles(generator, 2459).toArray();
        double[] actual = nextDoubles(generator, 2459).toArray();

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevDoubles(generator, 2467).toArray();
        actual = nextDoubles(generator, 2467).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevFloat() {
        Float[] expected = nextFloats(generator, 2459).toArray(Float[]::new);
        Float[] actual = prevFloats(generator, 2459).toArray(Float[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextFloats(generator, 2467).toArray(Float[]::new);
        actual = prevFloats(generator, 2467).toArray(Float[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextFloat() {
        Float[] expected = prevFloats(generator, 2459).toArray(Float[]::new);
        Float[] actual = nextFloats(generator, 2459).toArray(Float[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevFloats(generator, 2467).toArray(Float[]::new);
        actual = nextFloats(generator, 2467).toArray(Float[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevBoolean() {
        Boolean[] expected = nextBooleans(generator, 2459).toArray(Boolean[]::new);
        Boolean[] actual = prevBooleans(generator, 2459).toArray(Boolean[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextBooleans(generator, 2467).toArray(Boolean[]::new);
        actual = prevBooleans(generator, 2467).toArray(Boolean[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextBoolean() {
        Boolean[] expected = prevBooleans(generator, 2459).toArray(Boolean[]::new);
        Boolean[] actual = nextBooleans(generator, 2459).toArray(Boolean[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevBooleans(generator, 2467).toArray(Boolean[]::new);
        actual = nextBooleans(generator, 2467).toArray(Boolean[]::new);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @DataProvider(format = "%m %i")
    public static Object[][] dataProviderTestNextPrevBytes() {
        return new Object[][]{{2459}, {2467}, {256}, {257}, {258}, {259}};
    }

    @Test
    @UseDataProvider("dataProviderTestNextPrevBytes")
    public void testNextPrevBytes(int size) {
        byte[] expected1 = new byte[size];
        byte[] actual1 = new byte[size];

        generator.nextBytes(expected1);
        generator.prevBytes(actual1);
        assertThat(actual1, equalTo(reverseArray(expected1)));

        byte[] expected2 = new byte[size];
        byte[] actual2 = new byte[size];

        generator.nextBytes(expected2);
        generator.prevBytes(actual2);

        assertThat(actual2, equalTo(reverseArray(expected2)));
        assertThat(expected1, equalTo(expected2));
    }

    @Test
    @UseDataProvider("dataProviderTestNextPrevBytes")
    public void testPrevNextBytes(int size) {
        byte[] expected1 = new byte[size];
        byte[] actual1 = new byte[size];

        generator.prevBytes(expected1);
        generator.nextBytes(actual1);
        assertThat(actual1, equalTo(reverseArray(expected1)));

        byte[] expected2 = new byte[size];
        byte[] actual2 = new byte[size];

        generator.prevBytes(expected2);
        generator.nextBytes(actual2);

        assertThat(actual2, equalTo(reverseArray(expected2)));
        assertThat(expected1, equalTo(expected2));
    }

    @Test
    public void testNextBytes_NullBuffer() {
        assertThrows(NullPointerException.class,
                     () -> generator.nextBytes(null));
    }

    @Test
    public void testPrevBytes_NullBuffer() {
        assertThrows(NullPointerException.class,
                     () -> generator.prevBytes(null));
    }

    @Test
    public void testUndoNextGaussianEffect() {
        double n1 = generator.nextGaussian();
        double n2 = generator.nextGaussian();
        double n3 = generator.nextGaussian();
        generator.undoNextGaussian();
        generator.undoNextGaussian();
        generator.undoNextGaussian();

        assertThat(generator.nextGaussian(), equalTo(n1));
        assertThat(generator.nextGaussian(), equalTo(n2));
        assertThat(generator.nextGaussian(), equalTo(n3));
    }

    @SuppressWarnings("SameReturnValue")
    private static double advanceNextGaussian(ReverseRandomGenerator generator) {
        generator.nextGaussian();
        return 0;
    }

    @SuppressWarnings("SameReturnValue")
    private static double undoNextGaussianEffect(ReverseRandomGenerator generator) {
        generator.undoNextGaussian();
        return 0;
    }

    @Test
    public void testNextPrevMixedCalls() {
        Supplier<double[]> supplier = () -> new double[]{
                (double) generator.nextInt(),
                advanceNextGaussian(generator),
                (double) generator.nextInt(75),
                generator.nextDouble(),
                advanceNextGaussian(generator),
                (double) generator.nextFloat(),
                generator.nextBoolean() ? 1d : 0d,
                generator.nextLong(),
                advanceNextGaussian(generator)};

        Supplier<double[]> reverseSupplier = () -> new double[]{
                undoNextGaussianEffect(generator),
                generator.prevLong(),
                generator.prevBoolean() ? 1d : 0d,
                (double) generator.prevFloat(),
                undoNextGaussianEffect(generator),
                generator.prevDouble(),
                (double) generator.prevInt(75),
                undoNextGaussianEffect(generator),
                (double) generator.prevInt()};

        int LIMIT = 102_960; //lcd(9, 55, 624)
        double[] expected = Stream.generate(supplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();
        double[] actual = Stream.generate(reverseSupplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();

        assertThat(reverseArray(actual), equalTo(expected));

        double[] expected2 = Stream.generate(supplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();

        assertThat(expected2, equalTo(expected));
    }

    @Test
    public void testNextPrevMixedCallsNoGaussian() {
        //the gaussian breaks things up for some random generators
        Supplier<double[]> supplier = () -> new double[]{
                (double) generator.nextInt(),
                (double) generator.nextInt(75),
                generator.nextDouble(),
                (double) generator.nextFloat(),
                generator.nextBoolean() ? 1d : 0d,
                generator.nextLong(),
                generator.nextInt(90)};

        Supplier<double[]> reverseSupplier = () -> new double[]{
                generator.prevInt(90),
                generator.prevLong(),
                generator.prevBoolean() ? 1d : 0d,
                (double) generator.prevFloat(),
                generator.prevDouble(),
                (double) generator.prevInt(75),
                (double) generator.prevInt()};

        int LIMIT = 240_240; //lcd(7, 55, 624)
        double[] expected = Stream.generate(supplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();
        double[] actual = Stream.generate(reverseSupplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();

        assertThat(reverseArray(actual), equalTo(expected));

        double[] expected2 = Stream.generate(supplier)
                .flatMapToDouble(DoubleStream::of)
                .limit(LIMIT)
                .toArray();

        assertThat(expected2, equalTo(expected));
    }

    @Test
    public abstract void testNextInt16ExactValue();

    @Test
    public abstract void testNextIntExactValue();

    @Test
    public abstract void testNextIntWideRangeExactValue();

    @Test
    public abstract void testNextLong16ExactValue();

    @Test
    public abstract void testNextLongExactValue();

    @Test
    public abstract void testNextDoubleExactValue();

    @Test
    public abstract void testNextFloatExactValue();

    @Test
    public abstract void testNextBooleanExactValue();
}
