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
import static ro.derbederos.untwist.ArrayUtils.generateLongArray;

@RunWith(DataProviderRunner.class)
public class MersenneTwisterPy3kRandomGeneratorTest extends
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
