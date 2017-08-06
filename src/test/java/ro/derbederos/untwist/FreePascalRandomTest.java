package ro.derbederos.untwist;

import org.junit.Test;

import java.util.stream.LongStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class FreePascalRandomTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new FreePascalRandom(123456789013L);
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

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {7, 15, 8, 3, 4, 14, 2, 15, 5, 9};
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
        long[] expected = {9L, 9L, 13L, 13L, 13L, 6L, 14L, 13L, 15L, 1L};
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
        double[] expected = {0.4963318055961281, 0.965203438187018, 0.5140685348305851, 0.2487994299735874,
                0.2816693552304059, 0.9009337187744677, 0.1824139088857919, 0.9811860672198236,
                0.35981608484871686, 0.6221055367495865};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.4963318F, 0.96520346F, 0.51406854F, 0.24879943F, 0.28166935F,
                0.90093374F, 0.1824139F, 0.9811861F, 0.35981607F, 0.62210554F};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

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
}
