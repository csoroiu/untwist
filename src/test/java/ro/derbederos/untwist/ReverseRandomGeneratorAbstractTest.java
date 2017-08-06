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

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static ro.derbederos.untwist.ArrayUtils.*;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.reverseArray;

@Ignore
@RunWith(DataProviderRunner.class)
public abstract class ReverseRandomGeneratorAbstractTest<T extends ReverseRandomGenerator>
        extends RandomGeneratorAbstractTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * RandomGenerator under test
     */
    protected T generator;

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
        int[] actualInt = generateIntArray(10, () -> rInt.nextInt(1000));

        ReverseRandomGenerator rLong = makeGenerator();
        rLong.setSeed(seedLong);
        int[] actualLong = generateIntArray(10, () -> rLong.nextInt(1000));

        ReverseRandomGenerator rArray = makeGenerator();
        rArray.setSeed(seedArray);
        int[] actualArray = generateIntArray(10, () -> rArray.nextInt(1000));

        assertThat("IntVsLong", actualInt, equalTo(actualLong));
        assertThat("LongVsArray", actualLong, equalTo(actualArray));
    }

    @Test
    public void testSet64bitSeedLongVsArray() {
        final long seedLong = 0x1234567823456789L;
        final int[] seedArray = {0x12345678, 0x23456789};

        ReverseRandomGenerator rLong = makeGenerator();
        rLong.setSeed(seedLong);
        int[] actualLong = generateIntArray(10, () -> rLong.nextInt(1000));

        ReverseRandomGenerator rArray = makeGenerator();
        rArray.setSeed(seedArray);
        int[] actualArray = generateIntArray(10, () -> rArray.nextInt(1000));

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
        int[] expected = generateIntArray(2459, () -> generator.nextInt());
        int[] actual = generateIntArray(2459, () -> generator.prevInt());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.nextInt());
        actual = generateIntArray(2467, () -> generator.prevInt());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextInt() {
        int[] expected = generateIntArray(2459, () -> generator.prevInt());
        int[] actual = generateIntArray(2459, () -> generator.nextInt());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.prevInt());
        actual = generateIntArray(2467, () -> generator.nextInt());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevIntBounded() {
        int[] expected = generateIntArray(2459, () -> generator.nextInt(78209372));
        int[] actual = generateIntArray(2459, () -> generator.prevInt(78209372));

        IntStream.of(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.nextInt(78209372));
        actual = generateIntArray(2467, () -> generator.prevInt(78209372));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntBounded() {
        int[] expected = generateIntArray(2459, () -> generator.prevInt(78209372));
        int[] actual = generateIntArray(2459, () -> generator.nextInt(78209372));

        IntStream.of(expected).forEach((t) -> assertThat(t, between(0, 78209372)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.prevInt(78209372));
        actual = generateIntArray(2467, () -> generator.nextInt(78209372));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevLong() {
        long[] expected = generateLongArray(2459, () -> generator.nextLong());
        long[] actual = generateLongArray(2459, () -> generator.prevLong());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.nextLong());
        actual = generateLongArray(2467, () -> generator.prevLong());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLong() {
        long[] expected = generateLongArray(2459, () -> generator.prevLong());
        long[] actual = generateLongArray(2459, () -> generator.nextLong());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.prevLong());
        actual = generateLongArray(2467, () -> generator.nextLong());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    public void testNextPrevDouble() {
        double[] expected = generateDoubleArray(2459, () -> generator.nextDouble());
        double[] actual = generateDoubleArray(2459, () -> generator.prevDouble());

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateDoubleArray(2467, () -> generator.nextDouble());
        actual = generateDoubleArray(2467, () -> generator.prevDouble());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextDouble() {
        double[] expected = generateDoubleArray(2459, () -> generator.prevDouble());
        double[] actual = generateDoubleArray(2459, () -> generator.nextDouble());

        DoubleStream.of(expected).forEach((t) -> assertThat(t, between(0.0, 1.0)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateDoubleArray(2467, () -> generator.prevDouble());
        actual = generateDoubleArray(2467, () -> generator.nextDouble());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    public void testNextPrevFloat() {
        float[] expected = generateFloatArray(2459, () -> generator.nextFloat());
        float[] actual = generateFloatArray(2459, () -> generator.prevFloat());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateFloatArray(2467, () -> generator.nextFloat());
        actual = generateFloatArray(2467, () -> generator.prevFloat());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextFloat() {
        float[] expected = generateFloatArray(2459, () -> generator.prevFloat());
        float[] actual = generateFloatArray(2459, () -> generator.nextFloat());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateFloatArray(2467, () -> generator.prevFloat());
        actual = generateFloatArray(2467, () -> generator.nextFloat());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testNextPrevBoolean() {
        boolean[] expected = generateBooleanArray(2459, () -> generator.nextBoolean());
        boolean[] actual = generateBooleanArray(2459, () -> generator.prevBoolean());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateBooleanArray(2467, () -> generator.nextBoolean());
        actual = generateBooleanArray(2467, () -> generator.prevBoolean());

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextBoolean() {
        boolean[] expected = generateBooleanArray(2459, () -> generator.prevBoolean());
        boolean[] actual = generateBooleanArray(2459, () -> generator.nextBoolean());

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateBooleanArray(2467, () -> generator.prevBoolean());
        actual = generateBooleanArray(2467, () -> generator.nextBoolean());

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
}
