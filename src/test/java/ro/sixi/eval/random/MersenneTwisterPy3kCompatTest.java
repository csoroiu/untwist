package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.sixi.eval.random.Utils.between;
import static ro.sixi.eval.random.Utils.createStream;
import static ro.sixi.eval.util.ArrayUtils.generateBooleanArray;
import static ro.sixi.eval.util.ArrayUtils.generateDoubleArray;
import static ro.sixi.eval.util.ArrayUtils.generateIntArray;
import static ro.sixi.eval.util.ArrayUtils.generateLongArray;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MersenneTwisterPy3kCompatTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MersenneTwisterPy3kCompat r;

    @Before
    public void setup() {
        r = new MersenneTwisterPy3kCompat(1234567890);
    }

    @Test
    public void testSetSeedArray() {
        int[] largeseed = new int[625];
        Arrays.fill(largeseed, 0x01010101);
        largeseed[largeseed.length - 1] = 0x01010102;
        r.setSeed(largeseed);
        int[] expected = { 360, 239, 640, 729, 558, 92, 366, 913, 108, 132 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(1000));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testSetSeedLongVsSetSeedArray() {
        final long seedLong = 0x0304050601010102L;
        final int[] seedArray = { 0x03040506, 0x01010102 };
        final int[] expected = { 4, 815, 264, 766, 681, 399, 91, 22, 171, 420 };

        r.setSeed(seedLong);
        int[] actualLong = generateIntArray(expected.length, () -> r.nextInt(1000));

        r.setSeed(seedArray);
        int[] actualArray = generateIntArray(expected.length, () -> r.nextInt(1000));

        assertThat(actualLong, equalTo(expected));
        assertThat(actualArray, equalTo(expected));
    }

    @Test
    public void testIntNegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        r.nextInt(-16);
    }

    @Test
    public void testIntMaxValue() {
        int[] expected = { 1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130, 1286552655,
                1917026106, 1619555880 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongIntMaxValue() {
        long[] expected = { 1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130, 1286552655,
                1917026106, 1619555880 };
        long[] actual = generateLongArray(expected.length, () -> r.nextLong(Integer.MAX_VALUE));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongLongMaxValue() {
        final long[] expected = { 5378934912805100368L, 1481834513793674581L, 2022704902811851265L,
                5525701581272513140L, 6955939542478552692L, 2825459752566365625L, 8145320789793645473L,
                4067308899818932548L, 8059721601458305289L, 1476791508350122857L };
        final long[] actual = generateLongArray(expected.length, () -> r.nextLong(Long.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong32bit() {
        long[] expected = { 2727734613L, 1904908801L, 3470892473L, 360581444L, 1854258025L, 1304656966L, 1499749522L,
                3662865218L, 2732253452L, 3880916009L };
        final long _32bit = 1L << 32;
        long[] actual = generateLongArray(expected.length, () -> r.nextLong(_32bit));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test16() {
        int[] expected = { 5, 14, 7, 9, 8, 2, 14, 4, 13, 5 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong16() {
        long[] expected = { 5, 14, 7, 9, 8, 2, 14, 4, 13, 5 };
        long[] actual = generateLongArray(expected.length, () -> r.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test9() {
        int[] expected = { 2, 7, 3, 4, 4, 1, 7, 2, 6, 2 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDouble() {
        double[] expected = { 0.9206826283274985, 0.6351002019693018, 0.4435211436398484, 0.8068844348124993,
                0.8926848452848529, 0.8081301250035834, 0.25490020128427027, 0.08395441205038512, 0.13853413517651525,
                0.4317280885585699 };
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testFloat() {
        expectedException.expect(UnsupportedOperationException.class);
        r.nextFloat();
    }

    @Test
    public void testBoolean() {
        boolean[] expected = { true, true, true, false, false, false, true, true, true, true };
        boolean[] actual = generateBooleanArray(expected.length, () -> r.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testIntStream() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> r.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testDoubleStream() {
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, () -> r.nextDouble()).forEach((t) -> assertThat(t, betweenMatcher));
    }
}
