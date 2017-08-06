package ro.derbederos.untwist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class CLRRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CLRRandom generator;

    @Before
    public void setup() {
        generator = new CLRRandom(1234567890L);
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {8, 6, 4, 0, 5, 13, 10, 1, 12, 13};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_32bit() {
        int[] expected = {-287579909, 90175452, 80605103, 1593771972, 1778445194, -482557609, 1894541034, 1056929146,
                779980809, 1253822814, 1884515393, 614983788, -358924531, 298830117, 903849615, -549623606, 676576329,
                853008319, 370052958, 194295684};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(-1_000_000_000, Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {4, 3, 2, 0, 2, 7, 5, 0, 6, 7, 6, 6, 7, 4, 2, 2, 1, 8, 3, 8};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long[] expected = {-5048026723238850071L, -2890171289807960499L, 7060514762430744938L,
                7740928225207699870L, 1208723606135141233L, 7682170035453475444L, -5466162641285947888L,
                1934692803206552476L, 602691552480251525L, -1193777884039002094L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.547308153727701, 0.42220238895258044, 0.3072717289008534, 0.006450907330238682,
                0.31335299849200665, 0.8306209607192413, 0.6481559642814826, 0.07130287451264582, 0.7655025449420803,
                0.8250625430723012};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.54730815F, 0.42220238F, 0.30727172F, 0.0064509073F, 0.313353F, 0.83062094F, 0.648156F,
                0.071302876F, 0.7655026F, 0.8250625F};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {true, false, false, false, false, true, true, false, true, true, true, true, true, true,
                false, false, false, true, false, true};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }
}
