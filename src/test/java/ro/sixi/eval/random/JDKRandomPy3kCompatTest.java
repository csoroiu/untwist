package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.sixi.eval.util.ArrayUtils.generateDoubleArray;
import static ro.sixi.eval.util.ArrayUtils.generateFloatArray;
import static ro.sixi.eval.util.ArrayUtils.generateIntArray;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JDKRandomPy3kCompatTest {
    // openjdk random tests.
    // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/test/java/util/Random

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JDKRandomPy3kCompat r;

    @Before
    public void setup() {
        r = new JDKRandomPy3kCompat();
        r.setSeed(0L);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        final int seedInt = 0x12345678;
        final long seedLong = 0x12345678L;
        final int[] seedArray = { seedInt };
        final int[] expected = { 880, 671, 970, 214, 37, 891, 670, 23, 301, 904 };

        JDKRandomPy3kCompat rInt = new JDKRandomPy3kCompat();
        rInt.setSeed(seedInt);
        int[] actualInt = generateIntArray(expected.length, () -> rInt.nextInt(1000));

        JDKRandomPy3kCompat rLong = new JDKRandomPy3kCompat(seedLong);
        int[] actualLong = generateIntArray(expected.length, () -> rLong.nextInt(1000));

        JDKRandomPy3kCompat rArray = new JDKRandomPy3kCompat();
        rArray.setSeed(seedArray);
        int[] actualArray = generateIntArray(expected.length, () -> rArray.nextInt(1000));

        assertThat(actualInt, equalTo(expected));
        assertThat(actualLong, equalTo(expected));
        assertThat(actualArray, equalTo(expected));
    }

    @Test
    public void testSetSeedLong() {
        r.setSeed(42523532L);
        int expected = -1778905166;
        int actual = r.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[] { 96, -76, 32, -69, 56, 81, -39, -44 };
        byte[] actual = new byte[expected.length];
        r.nextBytes(actual);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_NoBound() {
        int expected = -1155484576;
        int actual = r.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_Lue() {
        int expected = 12;
        int actual = r.nextInt(42);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_Pow2() {
        int expected = 784870680;
        int actual = r.nextInt(1 << 30);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_IntOverflow() {
        r.setSeed(215660466117472l);
        int expected = 4224;
        int actual = r.nextInt(100000);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long expected = -4962768465676381896l;
        long actual = r.nextLong();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDoubleStream() {
        double[] expected = { 0.730967787376657, 0.24053641567148587, 0.6374174253501083, 0.5504370051176339,
                0.5975452777972018, 0.3332183994766498, 0.3851891847407185, 0.984841540199809, 0.8791825178724801,
                0.9412491794821144 };
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloatStream() {
        float[] expected = { 0.73096776F, 0.24053642F, 0.63741744F, 0.55043703F, 0.59754527F, 0.3332184F, 0.38518918F,
                0.9848415F, 0.8791825F, 0.9412492F };
        float[] actual = generateFloatArray(expected.length, () -> r.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double expected = 0.73096778737665700;
        double actual = r.nextDouble();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean expected = true;
        boolean actual = r.nextBoolean();
        assertThat(actual, equalTo(expected));
    }
}
