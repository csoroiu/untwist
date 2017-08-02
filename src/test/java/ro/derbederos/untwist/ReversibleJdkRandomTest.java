package ro.derbederos.untwist;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ReversibleJdkRandomTest extends ReverseRandomGeneratorAbstractTest<ReversibleJdkRandom> {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Override
    protected ReversibleJdkRandom makeGenerator() {
        return new ReversibleJdkRandom(1000);
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        try {
            generator.nextInt(-1);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            generator.nextInt(0);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    @Test
    public void testNextIntNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(-1);
    }

    @Test
    public void testPrevInt() {
        int expected = generator.nextInt(100);
        int actual = generator.prevInt(100);

        assertThat(expected, equalTo(87));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevIntNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.prevInt(-16);
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
}
