package ro.derbederos.untwist;

import org.junit.Test;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class TurboPascalRandomTest extends ReverseBitsStreamGeneratorAbstractTest<TurboPascalRandom> {

    @Override
    protected TurboPascalRandom makeGenerator() {
        return new TurboPascalRandom(0xC44002DC);
    }

    @Test
    public void testZero() {
        //-1498392781 seed was causing the pascal generator to generate 1.0 in turbo pascal 5 and 6.
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, false);
        assertThat(generator.nextDouble(), equalTo(0.5));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {12, 8, 11, 13, 12, 7, 3, 15, 2, 3};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {-845578675, -2088293502, -1339891317, -783150152, -964345191,
                1941516542, 936439031, -251921196, 703152165, 1070159034};
        int[] actual = generateIntArray(expected.length, (IntSupplier) generator::nextInt);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {2L, 8L, 14L, 4L, 10L, 0L, 6L, 12L, 2L, 8L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-3631732753113339006L, -5754789383197551688L, -4141831055458356994L, 4021975016885976276L,
                3020015553856754874L, -8904021482262177232L, -1975669623462997322L, 6926665487186659276L,
                5403570775623280370L, -4166240069301493336L};
        long[] actual = generateLongArray(expected.length, (LongSupplier) generator::nextLong);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.8031233728397638, 0.5137812797911465, 0.6880322422366589, 0.8176586460322142,
                0.7754708884749562, 0.4520445461384952, 0.21803170233033597, 0.9413450257852674,
                0.16371537116356194, 0.24916581669822335};
        double[] actual = generateDoubleArray(expected.length, generator::nextDouble);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.80312335F, 0.5137813F, 0.68803227F, 0.81765866F, 0.7754709F,
                0.45204455F, 0.2180317F, 0.94134504F, 0.16371538F, 0.24916582F};
        float[] actual = generateFloatArray(expected.length, generator::nextFloat);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {true, true, true, true, true, false, false, true, false, false,
                true, false, true, true, false, true, false, true, true, false};
        boolean[] actual = generateBooleanArray(expected.length, generator::nextBoolean);

        assertThat(actual, equalTo(expected));
    }
}
