package ro.derbederos.untwist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class MersenneTwisterPy3kTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MersenneTwisterPy3k generator;

    @Before
    public void setup() {
        generator = makeGenerator();
    }

    private MersenneTwisterPy3k makeGenerator() {
        return new MersenneTwisterPy3k(1234567890);
    }

    @Test
    public void testNextLong_LongMaxValue() {
        final long[] expected = {5378934912805100368L, 1481834513793674581L, 2022704902811851265L,
                5525701581272513140L, 6955939542478552692L, 2825459752566365625L, 8145320789793645473L,
                4067308899818932548L, 8059721601458305289L, 1476791508350122857L};
        final long[] actual = generateLongArray(expected.length, () -> generator.nextLong(Long.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_32bit() {
        long[] expected = {2727734613L, 1904908801L, 3470892473L, 360581444L, 1854258025L, 1304656966L, 1499749522L,
                3662865218L, 2732253452L, 3880916009L};
        final long _32bit = 1L << 32;
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(_32bit));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {5, 14, 7, 9, 8, 2, 14, 4, 13, 5};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_16() {
        long[] expected = {5, 14, 7, 9, 8, 2, 14, 4, 13, 5};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {2, 7, 3, 4, 4, 1, 7, 2, 6, 2};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.9206826283274985, 0.6351002019693018, 0.4435211436398484, 0.8068844348124993,
                0.8926848452848529, 0.8081301250035834, 0.25490020128427027, 0.08395441205038512, 0.13853413517651525,
                0.4317280885585699};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.9206826F, 0.6351002F, 0.44352114F, 0.8068844F, 0.8926848F, 0.80813015F, 0.2549002F,
                0.08395441F, 0.13853413F, 0.4317281F};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {true, true, true, false, false, false, true, true, true, true};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }
}
