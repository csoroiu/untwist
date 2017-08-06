package ro.derbederos.untwist;

import org.junit.Test;

import java.util.stream.LongStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.generateIntArray;

public class FreePascalRandomGeneratorTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new FreePascalRandom(123456789013L);
    }

    @Override
    int[] getMakotoNishimuraTestSeed() {
        return new int[]{0x456, 0x345, 0x234, 0x123};
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));

        nextInt = generator.nextInt(0);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    public void testPrevIntIAE2() {
        int prevInt = generator.prevInt(-1);
        assertThat(prevInt, equalTo(0));

        prevInt = generator.prevInt(0);
        assertThat(prevInt, equalTo(0));
    }

    @Override
    @Test
    public void testNextIntNeg() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    @Test
    public void testPrevIntNeg() {
        int prevInt = generator.prevInt(-1);
        assertThat(prevInt, equalTo(0));
    }

    @Override
    @Test
    public void testNextLongNeg() {
        long nextLong = generator.nextLong(-16);
        assertThat(nextLong, equalTo(9L));
    }

    @Override
    @Test
    public void testPrevLongNeg() {
        long prevLong = generator.prevLong(-16);
        assertThat(prevLong, equalTo(1L));
    }

    @Test
    public void testNextLong_0() {
        boolean result = LongStream.generate(() -> generator.nextLong(0))
                .limit(1000)
                .allMatch(value -> value == 0);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testPrevLong_0() {
        boolean result = LongStream.generate(() -> generator.prevLong(0))
                .limit(1000)
                .allMatch(value -> value == 0);

        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextInt_NegativeValue() {
        int[] expected = {-8, -15, -8, -4, -5, -14, -3, -15, -6, -10,
                -10, -3, -7, -8, -7, -10, -13, -5, -1, -11,
                -6, -14, -15, -15, -9, -12, -4, -8};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(-16));

        assertThat(actual, equalTo(expected));
    }
}
