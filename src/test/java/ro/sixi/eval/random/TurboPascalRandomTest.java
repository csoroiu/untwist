package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.sixi.eval.random.Utils.between;
import static ro.sixi.eval.random.Utils.createStream;
import static ro.sixi.eval.util.ArrayUtils.generateBooleanArray;
import static ro.sixi.eval.util.ArrayUtils.generateDoubleArray;
import static ro.sixi.eval.util.ArrayUtils.generateIntArray;
import static ro.sixi.eval.util.ArrayUtils.generateLongArray;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TurboPascalRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private TurboPascalRandom r;

    @Before
    public void setup() {
        r = new TurboPascalRandom(1234567890L);
    }

    @Test
    public void testIntNegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        r.nextInt(-16);
    }

    @Test
    public void testIntMaxValue() {
        int[] expected = { 939076365, 1451019587, 342299220, 1109061030, 1380499522, 1017810769, 734729370, 174788868,
                419817753, 505066367 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongValue() {
        long[] expected = { -1392928120L, -2076845234L, -6588343460521763164L, 6311277235765912074L,
                3606207044135511808L, -464022778L, -51589220L, -735761854L, -498207624L, 95602918877569982L };
        long[] actual = generateLongArray(expected.length, () -> r.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test16() {
        int[] expected = { 6, 10, 2, 8, 10, 7, 5, 1, 3, 3 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test9() {
        int[] expected = { 3, 6, 1, 4, 5, 4, 3, 0, 1, 2 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDouble() {
        double[] expected = { 0.937291509239003062, 0.175683649256825447, 0.659395495662465692, 0.016446787398308516,
                0.142845185240730643, 0.973955073393881321, 0.842135024489834905, 0.5813924097456038,
                0.695492875529453158, 0.735189855098724365 };
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDoubleCoprocDisabled() {
        double[] expected = { 0.43729150923900306, 0.67568364925682545, 0.15939549566246569, 0.51644678739830852,
                0.64284518524073064, 0.47395507339388132, 0.3421350244898349, 0.0813924097456038, 0.19549287552945316,
                0.23518985509872437 };
        TurboPascalRandom r = new TurboPascalRandom(1234567890, false);
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testFloat() {
        expectedException.expect(UnsupportedOperationException.class);

        r.nextFloat();
    }

    @Test
    public void testBoolean() {
        boolean[] expected = { true, false, true, false, false, true, true, true, true, true };
        boolean[] actual = generateBooleanArray(expected.length, () -> r.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testIntStream() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> r.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testDoubleStream() {
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, () -> r.nextDouble()).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testZero() {
        r.setSeed(Integer.toUnsignedLong(-1498392781));
        assertThat(r.nextDouble(), equalTo(0.0));
    }

    @Test
    public void testZeroCoprocDisabled() {
        TurboPascalRandom r = new TurboPascalRandom(-1498392781, false);
        assertThat(r.nextDouble(), equalTo(0.5));
    }
}
