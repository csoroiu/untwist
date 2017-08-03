package ro.derbederos.untwist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class FreePascalRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private FreePascalRandom generator;

    @Before
    public void setup() {
        generator = new FreePascalRandom();
        generator.setSeed(1234567890L);
    }

    @Test
    public void testNextInt_NegativeValue() {
        int[] expected = {-10, -6, -9, -3, -14, -4, -14, -7, -7, -13, -12, -3, -5, -3, -15};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(-16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_IntMaxValue() {
        int[] expected = {1328851648, 731237375, 1270502066, 320041495, 1908433477, 499156889, 1914814095, 927307221,
                982618676, 1814042781};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_NegativeValue() {
        long[] expected = {2, 6, 12, 1, 9, 14, 4, 0, 9, 4};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(-16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_IntMaxValue() {
        long[] expected = {1287685506L, 1673686469L, 1518527220L, 1096406138L, 631473891L, 504365730L, 534922973L,
                63348502L, 983415046L, 1939773091L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_LongMaxValue() {
        final long[] expected = {6281281229428446594L, 2749135515611866470L, 4287725035768038540L,
                7965508383203884321L, 6359136809580176489L, 3128970990868452750L, 3297195868290960004L,
                171953701487956256L, 1742057592483719353L, 3819555233715651620L};
        final long[] actual = generateLongArray(expected.length, () -> generator.nextLong(Long.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_32bit() {
        long[] expected = {2657703298L, 2541004134L, 3816866956L, 3829628193L, 1965237353L, 3342292366L, 1147030148L,
                4278243616L, 2319689913L, 2308637732L};
        final long _32bit = 1L << 32;
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(_32bit));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {9, 5, 9, 2, 14, 3, 14, 6, 7, 13};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_16() {
        long[] expected = {2, 6, 12, 1, 9, 14, 4, 0, 9, 4};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_0() {
        long[] expected = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(0));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {5, 3, 5, 1, 7, 2, 8, 3, 4, 7};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long[] expected = {6281281229428446594L, 2749135515611866470L, 4287725035768038540L, 7965508383203884321L,
                -2864235227274599319L, 3128970990868452750L, 3297195868290960004L, 171953701487956256L,
                1742057592483719353L, 3819555233715651620L, -3607525068081994452L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.6187947695143521, 0.3405089376028627, 0.5916236280463636, 0.14903093478642404,
                0.8886835901066661, 0.23243803973309696, 0.8916547971311957, 0.4318110744934529, 0.457567477831617,
                0.8447294970974326};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.6187947989f, 0.3405089378f, 0.5916236043f, 0.1490309387f, 0.8886836171f, 0.2324380428f,
                0.8916547894f, 0.4318110645f, 0.4575674832f, 0.8447294831f};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {true, false, true, false, true, false, true, false, false, true};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }
}
