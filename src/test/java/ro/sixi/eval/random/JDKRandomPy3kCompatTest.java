package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ro.sixi.eval.random.JDKRandomPy3kCompat;

public class JDKRandomPy3kCompatTest {
    //openjdk random tests.
    //http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/test/java/util/Random

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JDKRandomPy3kCompat r;

    @Before
    public void setup() {
        r = new JDKRandomPy3kCompat();
        r.setSeed(0);
    }

    @Test
    public void testSetSeed() {
        r.setSeed(42523532l);
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
    public void testNextInt() {
        int expected = -1155484576;
        int actual = r.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextIntLue() {
        int expected = 12;
        int actual = r.nextInt(42);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextIntPow2() {
        int expected = 784870680;
        int actual = r.nextInt(1<<30);
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
    @SuppressWarnings("deprecation")
    public void testFloat() {
        expectedException.expect(UnsupportedOperationException.class);
        r.nextFloat();
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
