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
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGeneratorAbstractTest;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static ro.derbederos.untwist.RandomUtils.*;
import static ro.derbederos.untwist.Utils.*;

@Ignore
@RunWith(DataProviderRunner.class)
public abstract class ReverseRandomGeneratorAbstractTest<T extends ReverseRandomGenerator>
        extends RandomGeneratorAbstractTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        final int seedInt = 0x12345678;
        final long seedLong = 0x12345678L;
        final int[] seedArray = {0x12345678};

        ReverseRandomGenerator rInt = makeGenerator();
        rInt.setSeed(seedInt);
        int[] actualInt = nextInts(10, 0, 1000, rInt).toArray();

        ReverseRandomGenerator rLong = makeGenerator();
        rLong.setSeed(seedLong);
        int[] actualLong = nextInts(10, 0, 1000, rLong).toArray();

        ReverseRandomGenerator rArray = makeGenerator();
        rArray.setSeed(seedArray);
        int[] actualArray = nextInts(10, 0, 1000, rArray).toArray();

        assertThat("IntVsLong", actualInt, equalTo(actualLong));
        assertThat("LongVsArray", actualLong, equalTo(actualArray));
    }

    @Test
    public void testSet64bitSeedLongVsArray() {
        final long seedLong = 0x1234567823456789L;
        final int[] seedArray = {0x12345678, 0x23456789};

        ReverseRandomGenerator rLong = makeGenerator();
        rLong.setSeed(seedLong);
        int[] actualLong = nextInts(10, 0, 1000, rLong).toArray();

        ReverseRandomGenerator rArray = makeGenerator();
        rArray.setSeed(seedArray);
        int[] actualArray = nextInts(10, 0, 1000, rArray).toArray();

        assertThat("LongVsArray", actualLong, equalTo(actualArray));
    }

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
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    @Test
    public void testNextIntNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(-1);
    }

    @Test
    public void testPrevIntNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.prevInt(-1);
    }

    @Test
    public void testNextPrevInt() {
        int[] expected = nextInts(2459, generator).toArray();
        int[] actual = prevInts(2459, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextInts(2467, generator).toArray();
        actual = prevInts(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextInt() {
        int[] expected = prevInts(2459, generator).toArray();
        int[] actual = nextInts(2459, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevInts(2467, generator).toArray();
        actual = nextInts(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevIntBounded() {
        int[] expected = nextInts(2459, 0, 78209372, generator).toArray();
        int[] actual = prevInts(2459, 0, 78209372, generator).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextInts(2467, 0, 78209372, generator).toArray();
        actual = prevInts(2467, 0, 78209372, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntBounded() {
        int[] expected = prevInts(2459, 0, 78209372, generator).toArray();
        int[] actual = nextInts(2459, 0, 78209372, generator).toArray();

        stream(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevInts(2467, 0, 78209372, generator).toArray();
        actual = nextInts(2467, 0, 78209372, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevLong() {
        long[] expected = nextLongs(2459, generator).toArray();
        long[] actual = prevLongs(2459, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextLongs(2467, generator).toArray();
        actual = prevLongs(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLong() {
        long[] expected = prevLongs(2459, generator).toArray();
        long[] actual = nextLongs(2459, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevLongs(2467, generator).toArray();
        actual = nextLongs(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    public void testNextPrevDouble() {
        double[] expected = nextDoubles(2459, generator).toArray();
        double[] actual = prevDoubles(2459, generator).toArray();

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextDoubles(2467, generator).toArray();
        actual = prevDoubles(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextDouble() {
        double[] expected = prevDoubles(2459, generator).toArray();
        double[] actual = nextDoubles(2459, generator).toArray();

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevDoubles(2467, generator).toArray();
        actual = nextDoubles(2467, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    public void testNextPrevFloat() {
        float[] expected = nextFloats(2459, generator);
        float[] actual = prevFloats(2459, generator);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextFloats(2467, generator);
        actual = prevFloats(2467, generator);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextFloat() {
        float[] expected = prevFloats(2459, generator);
        float[] actual = nextFloats(2459, generator);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevFloats(2467, generator);
        actual = nextFloats(2467, generator);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevBoolean() {
        boolean[] expected = nextBooleans(2459, generator);
        boolean[] actual = prevBooleans(2459, generator);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextBooleans(2467, generator);
        actual = prevBooleans(2467, generator);

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextBoolean() {
        boolean[] expected = prevBooleans(2459, generator);
        boolean[] actual = nextBooleans(2459, generator);

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevBooleans(2467, generator);
        actual = nextBooleans(2467, generator);

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
        expectedException.expect(NullPointerException.class);

        generator.nextBytes(null);
    }

    @Test
    public void testPrevBytes_NullBuffer() {
        expectedException.expect(NullPointerException.class);

        generator.nextBytes(null);
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
    public abstract void testNextLongExactValue();

    @Test
    public abstract void testNextDoubleExactValue();

    @Test
    public abstract void testNextFloatExactValue();

    @Test
    public abstract void testNextBooleanExactValue();
}
