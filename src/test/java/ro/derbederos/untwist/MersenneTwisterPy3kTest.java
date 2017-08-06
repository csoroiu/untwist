package ro.derbederos.untwist;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.LongSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.generateBooleanArray;
import static ro.derbederos.untwist.ArrayUtils.generateDoubleArray;
import static ro.derbederos.untwist.ArrayUtils.generateIntArray;
import static ro.derbederos.untwist.ArrayUtils.generateLongArray;

@RunWith(DataProviderRunner.class)
public class MersenneTwisterPy3kTest extends
        ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new MersenneTwisterPy3k(123456789013L);
    }

    @Override
    int[] getMakotoNishimuraTestSeed() {
        return new int[]{0x456, 0x345, 0x234, 0x123};
    }

    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        super.testSet32BitSeedIntVsLongVsArray();
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {15, 7, 9, 5, 11, 4, 13, 14, 9, 1};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {2131728873, -149450095, -2087059751, 1068585415, 1209760669,
                -425486438, 783461773, -80805226, 1545398317, -1623044361};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt());

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {15L, 7L, 9L, 5L, 11L, 4L, 13L, 14L, 9L, 1L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-641883268277364247L, 4589539412615495385L, -1827450334891770979L, -347055802232427123L,
                -6970922448906819539L, 2488676750358164198L, -8896639325777151682L, -6782370575323180803L,
                5196967370074779647L, -5701509883458360255L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.4963318106919783, 0.5140685308635192, 0.2816693551907965, 0.18241391316939937,
                0.35981608645696583, 0.632311993352409, 0.4229770415850893, 0.44843774760013033,
                0.8226690238842976, 0.03549077131539968};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        double[] expected = {0.4963318109512329, 0.5140685439109802, 0.28166934847831726, 0.18241390585899353,
                0.3598160743713379, 0.632311999797821, 0.4229770302772522, 0.44843775033950806,
                0.8226690292358398, 0.035490769892930984};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {false, true, true, false, false, true, false, true, false, true,
                true, false, false, true, false, true, true, false, false, true};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextIntVsLongRangeIntMaxValue() {
        ReversibleMersenneTwister generator1 = makeGenerator();
        ReversibleMersenneTwister generator2 = makeGenerator();
        long[] expected = generateLongArray(700, () -> generator1.nextInt(Integer.MAX_VALUE));
        long[] actual = generateLongArray(700, () -> generator2.nextLong(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLongVsByteArray() {
        ReversibleMersenneTwister generator1 = makeGenerator();
        ReversibleMersenneTwister generator2 = makeGenerator();
        long[] expected = generateLongArray(700, (LongSupplier) generator1::nextLong);

        byte[] b = new byte[8];
        long[] actual = generateLongArray(700, () -> {
            generator2.nextBytes(b);
            return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
        });

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
}
