package ro.derbederos.untwist;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class TurboPascalRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private TurboPascalRandom generator;

    @Before
    public void setup() {
        generator = new TurboPascalRandom(1234567890L);
    }


    @Test
    public void testNextInt_IntMaxValue() {
        int[] expected = {939076365, 1451019587, 342299220, 1109061030, 1380499522, 1017810769, 734729370, 174788868,
                419817753, 505066367};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {6, 10, 2, 8, 10, 7, 5, 1, 3, 3};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {3, 6, 1, 4, 5, 4, 3, 0, 1, 2};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long[] expected = {8066604559440124552L, 2940327917205707598L, -6588343460521763164L,
                6311277235765912074L, 3606207044135511808L, -1286055861930192122L, 7328754210398064540L,
                7533534874542877250L, -5722989582491978632L, 95602918877569982L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.937291509239003062, 0.175683649256825447, 0.659395495662465692, 0.016446787398308516,
                0.142845185240730643, 0.973955073393881321, 0.842135024489834905, 0.5813924097456038,
                0.695492875529453158, 0.735189855098724365};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.9372915F, 0.17568365F, 0.6593955F, 0.016446788F, 0.14284518F, 0.9739551F, 0.842135F,
                0.5813924F, 0.69549286F, 0.73518986F};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble_CoprocDisabled() {
        TurboPascalRandom generator = new TurboPascalRandom(1234567890, false);
        double[] expected = {0.43729150923900306, 0.67568364925682545, 0.15939549566246569, 0.51644678739830852,
                0.64284518524073064, 0.47395507339388132, 0.3421350244898349, 0.0813924097456038, 0.19549287552945316,
                0.23518985509872437};
        double[] actual = generateDoubleArray(expected.length, generator::nextDouble);

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat_CoprocDisabled() {
        TurboPascalRandom generator = new TurboPascalRandom(1234567890, false);
        float[] expected = {0.4372915F, 0.6756837F, 0.1593955F, 0.51644677F, 0.6428452F, 0.47395507F, 0.342135F,
                0.08139241F, 0.19549288F, 0.23518986F};
        float[] actual = generateFloatArray(expected.length, generator::nextFloat);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {false, true, false, true, true, false, false, false, false, false};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt() {
        int expected = generator.nextInt(100);
        int actual = generator.prevInt(100);

        assertThat(expected, equalTo(43));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_16() {
        int[] expected = {3, 3, 1, 5, 7, 10, 8, 2, 10, 6};
        for (int i = 0; i < 10; i++) {
            generator.nextInt(16);
        }
        int[] actual = generateIntArray(expected.length, () -> generator.prevInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_NoBound() {
        int expected = generator.nextInt();
        int actual = generator.prevInt();

        assertThat(expected, equalTo(1878152731));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevLong() {
        long[] expected = {95602918877569982L, -5722989582491978632L, 7533534874542877250L,
                7328754210398064540L, -1286055861930192122L, 3606207044135511808L, 6311277235765912074L,
                -6588343460521763164L, 2940327917205707598L, 8066604559440124552L};

        for (int i = 0; i < 10; i++) {
            generator.nextLong();
        }

        long[] actual = generateLongArray(expected.length, () -> generator.prevLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevDouble() {
        double[] expected = {0.735189855098724365, 0.695492875529453158, 0.5813924097456038, 0.842135024489834905,
                0.973955073393881321, 0.142845185240730643, 0.016446787398308516, 0.659395495662465692,
                0.175683649256825447, 0.937291509239003062};

        for (int i = 0; i < 10; i++) {
            generator.nextDouble();
        }

        double[] actual = generateDoubleArray(expected.length, () -> generator.prevDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testPrevFloat() {
        float[] expected = {0.73518986F, 0.69549286F, 0.5813924F, 0.842135F, 0.9739551F, 0.14284518F,
                0.016446788F, 0.6593955F, 0.17568365F, 0.9372915F};

        for (int i = 0; i < 10; i++) {
            generator.nextFloat();
        }

        float[] actual = generateFloatArray(expected.length, () -> generator.prevFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevDouble_CoprocDisabled() {
        TurboPascalRandom generator = new TurboPascalRandom(1234567890, false);
        double[] expected = {0.23518985509872437, 0.19549287552945316, 0.0813924097456038, 0.3421350244898349,
                0.47395507339388132, 0.64284518524073064, 0.51644678739830852, 0.15939549566246569,
                0.67568364925682545, 0.43729150923900306};

        for (int i = 0; i < 10; i++) {
            generator.nextDouble();
        }

        double[] actual = generateDoubleArray(expected.length, generator::prevDouble);

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testPrevFloat_CoprocDisabled() {
        TurboPascalRandom generator = new TurboPascalRandom(1234567890, false);
        float[] expected = {0.23518986F, 0.19549288F, 0.08139241F, 0.342135F, 0.47395507F, 0.6428452F,
                0.51644677F, 0.1593955F, 0.6756837F, 0.4372915F};

        for (int i = 0; i < 10; i++) {
            generator.nextFloat();
        }

        float[] actual = generateFloatArray(expected.length, generator::prevFloat);

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBoolean() {
        boolean[] expected = {false, false, false, false, false, true, true, false, true, false};
        for (int i = 0; i < 10; i++) {
            generator.nextBoolean();
        }

        boolean[] actual = generateBooleanArray(expected.length, () -> generator.prevBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBytes() {
        byte[] nextBytes = new byte[]{27, 86, -14, 111, -120, -102, -7, -84};
        byte[] prevBytes = new byte[]{-84, -7, -102, -120, 111, -14, 86, 27};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_not32bitmultiple() {
        byte[] nextBytes = new byte[]{27, 86, -14, 111, -120, -102, -7};
        byte[] prevBytes = new byte[]{-7, -102, -120, 111, -14, 86, 27};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }
}
