package ro.sixi.eval.random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ReversibleJdkRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ReversibleJdkRandom r;

    @Before
    public void setup() {
        r = new ReversibleJdkRandom(1000);
    }

    @Test
    public void testPrevInt() {
        int expected = r.nextInt(100);
        int actual = r.prevInt(100);

        assertThat(expected, equalTo(87));
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

        assertThat(expected, equalTo(-1244746321));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_overflow() {
        r.setSeed(215660466117472L);
        int expected = r.nextInt(100000);
        int actual = r.prevInt(100000);
        int actualReverse = r.prevInt(100000);
        int expectedReverse = r.nextInt(100000);

        assertThat(expected, equalTo(4224));
        assertThat(actual, equalTo(expected));
        assertThat(actualReverse, equalTo(65354));
        assertThat(actualReverse, equalTo(expectedReverse));
    }

    @Test
    public void testPrevLong_NoBound() {
        long expected = r.nextLong();
        long actual = r.prevLong();

        assertThat(expected, equalTo(-5346144739450824145L));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBytes() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53, 63};
        byte[] prevBytes = new byte[]{63, 53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_not32bitmultiple() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53};
        byte[] prevBytes = new byte[]{53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }
}
