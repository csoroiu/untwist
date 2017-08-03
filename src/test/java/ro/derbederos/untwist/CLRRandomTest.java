package ro.derbederos.untwist;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.IntPredicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static ro.derbederos.untwist.ArrayUtils.*;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.betweenPredicate;
import static ro.derbederos.untwist.Utils.createStream;

public class CLRRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CLRRandom generator;

    @Before
    public void setup() {
        generator = new CLRRandom(1234567890L);
    }

    @Test
    public void testNextInt_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(-16);
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

    @Test
    @Ignore
    public void testNextInt_NoBoundStream() {
        IntPredicate betweenPredicate = betweenPredicate(0, Integer.MAX_VALUE);
        boolean result = createStream(1_000_000_000L, () -> generator.nextInt()).allMatch(betweenPredicate);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextIntStream() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> generator.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextInt_RangeStream() {
        final Matcher<Integer> betweenMatcher = between(-1000, 3000);
        createStream(100000, () -> generator.nextInt(-1000, 3000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(200, 100);
    }

    @Test
    public void testNextDoubleStream() {
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, generator::nextDouble).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextBytes_NullBuffer() {
        expectedException.expect(NullPointerException.class);

        generator.nextBytes(null);
    }

    private static final int COMPARE_STEPS = 100_000;

    @Test
    public void testStateNextPrev() {
        CLRRandom clrRandom = new CLRRandom(new int[]{291, 564, 837, 1110});
        int[] expected = clrRandom.getState();

        //going forward
        clrRandom.nextInt();
        //going back to the initial state
        clrRandom.prevInt();

        int[] actual = clrRandom.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;
        CLRRandom clrRandom = new CLRRandom(new int[]{291, 564, 837, 1110});

        for (int i = 0; i < COMPARE_STEPS; i++, clrRandom.nextInt()) {
            int[] expected = clrRandom.getState();

            //going forward
            clrRandom.nextInt();
            //going back to the initial state
            clrRandom.prevInt();

            int[] actual = clrRandom.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNext() {
        CLRRandom clrRandom = new CLRRandom(new int[]{291, 564, 837, 1110});
        int[] expected = clrRandom.getState();

        //going back
        clrRandom.prevInt();
        //going forward to the initial state
        clrRandom.nextInt();

        int[] actual = clrRandom.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;
        CLRRandom clrRandom = new CLRRandom(new int[]{291, 564, 837, 1110});

        for (int i = 0; i < COMPARE_STEPS; i++, clrRandom.prevInt()) {
            int[] expected = clrRandom.getState();

            //going back
            clrRandom.prevInt();
            //going forward to the initial state
            clrRandom.nextInt();

            int[] actual = clrRandom.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextVsNextPrev() {
        CLRRandom clrRandom1 = new CLRRandom(new int[]{291, 564, 837, 1110});
        clrRandom1.prevInt();
        clrRandom1.nextInt();
        int[] expected = clrRandom1.getState();


        CLRRandom clrRandom2 = new CLRRandom(new int[]{291, 564, 837, 1110});
        clrRandom2.prevInt();
        clrRandom2.nextInt();
        int[] actual = clrRandom2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
