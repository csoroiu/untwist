package ro.sixi.eval.lab;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReversibleRandomTest {

    private ReversibleRandom r;

    @Before
    public void setup() {
        r = new ReversibleRandom(0);
    }

    @Test
    public void testPrevInt() {
        int expected = r.nextInt(100);
        int actual = r.prevInt(100);

        assertThat(expected, equalTo(60));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_overflow() {
        r.setSeed(215660466117472l);
        int expected = r.nextInt(100000);
        int actual = r.prevInt(100000);

        assertThat(expected, equalTo(4224));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBytes() {
        byte[] nextBytes = new byte[] { 96, -76, 32, -69, 56, 81, -39, -44 };
        byte[] prevBytes = new byte[] { -44, -39, 81, 56, -69, 32, -76, 96 };
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_() {
        byte[] nextBytes = new byte[] { 96, -76, 32, -69, 56, 81, -39 };
        byte[] prevBytes = new byte[] { -44, -39, 81, 56, -69, 32, -76 };
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        r.nextBytes(expected);
        r.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }
}
