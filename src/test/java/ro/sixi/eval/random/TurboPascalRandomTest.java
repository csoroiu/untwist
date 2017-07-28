package ro.sixi.eval.random;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.sixi.eval.random.ArrayUtils.*;
import static ro.sixi.eval.random.Utils.between;
import static ro.sixi.eval.random.Utils.createStream;

public class TurboPascalRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private TurboPascalRandom r;

    @Before
    public void setup() {
        r = new TurboPascalRandom(1234567890L);
    }

    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        final int seedInt = 0x12345678;
        final long seedLong = 0x12345678L;
        final int[] seedArray = {seedInt};
        final int[] expected = {794, 476, 850, 946, 898, 885, 613, 790, 353, 868};

        TurboPascalRandom rInt = new TurboPascalRandom(seedInt);
        int[] actualInt = generateIntArray(expected.length, () -> rInt.nextInt(1000));

        TurboPascalRandom rLong = new TurboPascalRandom(seedLong);
        int[] actualLong = generateIntArray(expected.length, () -> rLong.nextInt(1000));

        TurboPascalRandom rArray = new TurboPascalRandom(seedArray);
        int[] actualArray = generateIntArray(expected.length, () -> rArray.nextInt(1000));

        assertThat(actualInt, equalTo(expected));
        assertThat(actualLong, equalTo(expected));
        assertThat(actualArray, equalTo(expected));
    }

    @Test
    public void testNextInt_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        r.nextInt(-16);
    }

    @Test
    public void testNextInt_NoBound() {
        int expected = 1878152731;
        int actual = r.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_IntMaxValue() {
        int[] expected = {939076365, 1451019587, 342299220, 1109061030, 1380499522, 1017810769, 734729370, 174788868,
                419817753, 505066367};
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {6, 10, 2, 8, 10, 7, 5, 1, 3, 3};
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {3, 6, 1, 4, 5, 4, 3, 0, 1, 2};
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long[] expected = {-1392928120L, -2076845234L, -6588343460521763164L, 6311277235765912074L,
                3606207044135511808L, -464022778L, -51589220L, -735761854L, -498207624L, 95602918877569982L};
        long[] actual = generateLongArray(expected.length, () -> r.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.937291509239003062, 0.175683649256825447, 0.659395495662465692, 0.016446787398308516,
                0.142845185240730643, 0.973955073393881321, 0.842135024489834905, 0.5813924097456038,
                0.695492875529453158, 0.735189855098724365};
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.9372915F, 0.17568365F, 0.6593955F, 0.016446788F, 0.14284518F, 0.9739551F, 0.842135F,
                0.5813924F, 0.69549286F, 0.73518986F};
        float[] actual = generateFloatArray(expected.length, () -> r.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble_CoprocDisabled() {
        TurboPascalRandom r = new TurboPascalRandom(1234567890, false);
        double[] expected = {0.43729150923900306, 0.67568364925682545, 0.15939549566246569, 0.51644678739830852,
                0.64284518524073064, 0.47395507339388132, 0.3421350244898349, 0.0813924097456038, 0.19549287552945316,
                0.23518985509872437};
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat_CoprocDisabled() {
        TurboPascalRandom r = new TurboPascalRandom(1234567890, false);
        float[] expected = {0.4372915F, 0.6756837F, 0.1593955F, 0.51644677F, 0.6428452F, 0.47395507F, 0.342135F,
                0.08139241F, 0.19549288F, 0.23518986F};
        float[] actual = generateFloatArray(expected.length, () -> r.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {true, false, true, false, false, true, true, true, true, true};
        boolean[] actual = generateBooleanArray(expected.length, () -> r.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextIntStream() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> r.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextDoubleStream() {
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, () -> r.nextDouble()).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testZero() {
        r.setSeed(Integer.toUnsignedLong(-1498392781));
        assertThat(r.nextDouble(), equalTo(0.0));
    }

    @Test
    public void testZero_CoprocDisabled() {
        TurboPascalRandom r = new TurboPascalRandom(-1498392781, false);
        assertThat(r.nextDouble(), equalTo(0.5));
    }

    @Test
    public void testPrevInt() {
        int expected = r.nextInt(100);
        int actual = r.prevInt(100);

        assertThat(expected, equalTo(43));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_16() {
        int[] expected = {3, 3, 1, 5, 7, 10, 8, 2, 10, 6};
        for (int i = 0; i < 10; i++) {
            r.nextInt(16);
        }
        int[] actual = generateIntArray(expected.length, () -> r.prevInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        r.prevInt(-16);
    }

    @Test
    public void testPrevInt_NoBound() {
        int expected = r.nextInt();
        int actual = r.prevInt();

        assertThat(expected, equalTo(1878152731));
        assertThat(actual, equalTo(expected));
    }
}
