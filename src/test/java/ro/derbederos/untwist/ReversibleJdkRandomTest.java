package ro.derbederos.untwist;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class ReversibleJdkRandomTest extends ReverseRandomGeneratorAbstractTest<ReversibleJdkRandom> {

    @Override
    protected ReversibleJdkRandom makeGenerator() {
        return new ReversibleJdkRandom(1000);
    }

    @Test
    public void testPrevInt() {
        int expected = generator.nextInt(100);
        int actual = generator.prevInt(100);

        assertThat(expected, equalTo(87));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt2N() {
        int expected = generator.nextInt(2 << 19);
        int actual = generator.prevInt(2 << 19);

        assertThat(expected, equalTo(744682));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_NoBound() {
        int expected = generator.nextInt();
        int actual = generator.prevInt();

        assertThat(expected, equalTo(-1244746321));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_overflow() {
        generator.setSeed(215660466117472L);
        int expected = generator.nextInt(100000);
        int actual = generator.prevInt(100000);
        int actualReverse = generator.prevInt(100000);
        int expectedReverse = generator.nextInt(100000);

        assertThat(expected, equalTo(4224));
        assertThat(actual, equalTo(expected));
        assertThat(actualReverse, equalTo(65354));
        assertThat(actualReverse, equalTo(expectedReverse));
    }

    @Test
    public void testPrevLong_NoBound() {
        long expected = generator.nextLong();
        long actual = generator.prevLong();

        assertThat(expected, equalTo(-5346144739450824145L));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBytes() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53, 63};
        byte[] prevBytes = new byte[]{63, 53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_not32bitmultiple() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53};
        byte[] prevBytes = new byte[]{53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {11, 3, 9, 7, 15, 0, 0, 13, 7, 5};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {-1244746321, 1060493871, -1826063944, 1976922248, -230127712,
                68408698, 169247282, -735843605, 2089114528, 1533708900};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt());

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-5346144739450824145L, -7842884917907853176L, -988390996874898054L, 726911540391045867L,
                8972678576892185188L, 8222391730744523982L, -7363680848376404625L, -8294095627538487754L,
                -6307709242837825884L, -470456323649602622L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong());

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.7101849056320707, 0.574836350385667, 0.9464192094792073, 0.039405954311386604,
                0.4864098780914311, 0.4457367367074283, 0.6008140654988429, 0.550376169584217,
                0.6580583901495688, 0.9744965039734514};
        double[] actual = generateDoubleArray(expected.length, () -> generator.nextDouble());

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.7101849F, 0.24691546F, 0.5748363F, 0.46028805F, 0.9464192F,
                0.015927613F, 0.039405942F, 0.828673F, 0.48640984F, 0.3570944F};
        float[] actual = generateFloatArray(expected.length, () -> generator.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {true, false, true, false, true, false, false, true, false, false,
                false, false, true, false, true, true, true, false, true, false};
        boolean[] actual = generateBooleanArray(expected.length, () -> generator.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCalls() {
        super.testNextPrevMixedCalls();
    }
}
