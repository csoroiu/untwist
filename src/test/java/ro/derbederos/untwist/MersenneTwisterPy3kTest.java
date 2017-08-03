package ro.derbederos.untwist;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.createStream;

@RunWith(DataProviderRunner.class)
public class MersenneTwisterPy3kTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MersenneTwisterPy3k generator;

    @Before
    public void setup() {
        generator = new MersenneTwisterPy3k(1234567890);
    }

    @Test
    public void testSetSeedArray() {
        int[] largeseed = new int[625];
        Arrays.fill(largeseed, 0x01010101);
        largeseed[largeseed.length - 1] = 0x01010102;
        generator.setSeed(largeseed);
        int[] expected = {360, 239, 640, 729, 558, 92, 366, 913, 108, 132};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(1000));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(-16);
    }

    @Test
    public void testNextInt_IntMaxValue() {
        int[] expected = {1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130, 1286552655,
                1917026106, 1619555880};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextLong(-16);
    }

    @Test
    public void testNextLong_IntMaxValue() {
        long[] expected = {1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130,
                1286552655, 1917026106, 1619555880};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(Integer.MAX_VALUE));
        assertThat(actual, equalTo(expected));
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
    public void testNextLong() {
        long[] expected = {-7688874252053652656L, 2963669024859614549L, 4045409808013761025L, -7395340914630067596L,
                -4534864988291531148L, 5650919505956806073L, -2156102495217048671L, 8134617799277283652L,
                -2327300871387940599L, 2953583019140954985L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBytes_asLongArray() {
        long[] expected = {-7688874252053652656L, 2963669024859614549L, 4045409808013761025L, -7395340914630067596L,
                -4534864988291531148L, 5650919505956806073L, -2156102495217048671L, 8134617799277283652L,
                -2327300871387940599L, 2953583019140954985L};

        final byte[] b = new byte[8];

        // The generated BigInteger's are compatible with the python numbers
        // returned by getrandbits function
        long[] actual = generateLongArray(expected.length, () -> {
            generator.nextBytes(b);
            return bytesToLong(b);
        });

        assertThat(actual, equalTo(expected));
    }

    @DataProvider(format = "%m %i")
    public static Object[][] dataProviderTestNextBytes() {
        //@formatter:off
        return new Object[][]{
                {new byte[]{80, -37, -79, -21, -102, -95, 75, -107}},
                {new byte[]{80, -37, -79, -21, -95, 75, -107}},
                {new byte[]{80, -37, -79, -21, 75, -107}},
                {new byte[]{80, -37, -79, -21, -107}},
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
                {new byte[]{0, 0, 80, -37, -79, -21, -102, -95, 75, -107, 0}},
                {new byte[]{0, 0, 80, -37, -79, -21, -95, 75, -107, 0}},
                {new byte[]{0, 0, 80, -37, -79, -21, 75, -107, 0}},
                {new byte[]{0, 0, 80, -37, -79, -21, -107, 0}},
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

    @Test
    public void testNextBytes_NullBuffer() {
        expectedException.expect(NullPointerException.class);

        generator.nextBytes(null);
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

    @Test
    public void testNextIntStream() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> generator.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextDoubleStream() {
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, () -> generator.nextDouble()).forEach((t) -> assertThat(t, betweenMatcher));
    }

    private static long bytesToLong(byte[] littleEndian) {
        ByteBuffer buffer = ByteBuffer.wrap(littleEndian).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }
}
