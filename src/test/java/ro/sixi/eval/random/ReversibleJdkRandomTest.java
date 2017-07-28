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
        r = new ReversibleJdkRandom(0);
    }

    @Test
    public void testPrevInt() {
        int expected = r.nextInt(100);
        int actual = r.prevInt(100);

        assertThat(expected, equalTo(60));
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

        assertThat(expected, equalTo(-1155484576));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_overflow() {
        r.setSeed(215660466117472l);
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
    public void testPrevBytes() {
        byte[] nextBytes = new byte[]{96, -76, 32, -69, 56, 81, -39, -44};
        byte[] prevBytes = new byte[]{-44, -39, 81, 56, -69, 32, -76, 96};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_not32bitmultiple() {
        byte[] nextBytes = new byte[]{96, -76, 32, -69, 56, 81, -39};
        byte[] prevBytes = new byte[]{-44, -39, 81, 56, -69, 32, -76};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytesMirror() {
        byte[] nextBytes = new byte[]{96, -76, 32, -69, 56, 81, -39, -44};
        byte[] prevBytes = new byte[]{-44, -39, 81, 56, -69, 32, -76, 96};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytesMirror(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytesMirror_not32bitmultiple() {
        byte[] nextBytes = new byte[]{96, -76, 32, -69, 56, 81, -39};
        byte[] prevBytes = new byte[]{-39, 81, 56, -69, 32, -76, 96};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytesMirror(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }
}
