package ro.sixi.eval.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;

public class MersenneTwisterPy3kCompatTest {

    private MersenneTwisterPy3kCompat r;

    @Before
    public void setup() {
        r = new MersenneTwisterPy3kCompat(1234567890);
    }

    @Test
    public void testIntMaxValue() {
        int expected[] = { 1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130, 1286552655,
                1917026106, 1619555880 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(Integer.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongIntMaxValue() {
        long expected[] = { 1977150888, 1252380877, 1363867306, 345016483, 952454400, 470947684, 1732771130,
                1286552655, 1917026106, 1619555880 };
        long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(Integer.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongLongMaxValue() {
        final long expected[] = { 5378934912805100368L, 1481834513793674581L, 2022704902811851265L,
                5525701581272513140L, 6955939542478552692L, 2825459752566365625L, 8145320789793645473L,
                4067308899818932548L, 8059721601458305289L, 1476791508350122857L };
        final long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(Long.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong32bit() {
        long expected[] = { 2727734613L, 1904908801L, 3470892473L, 360581444L, 1854258025L, 1304656966L, 1499749522L,
                3662865218L, 2732253452L, 3880916009L };
        long actual[] = new long[expected.length];
        final long _32bit = 1L << 32;
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(_32bit);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test16() {
        int expected[] = { 5, 14, 7, 9, 8, 2, 14, 4, 13, 5 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(16);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong16() {
        long expected[] = { 5, 14, 7, 9, 8, 2, 14, 4, 13, 5 };
        long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(16);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test9() {
        int expected[] = { 2, 7, 3, 4, 4, 1, 7, 2, 6, 2 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(9);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDouble() {
        double expected[] = { 0.9206826283274985, 0.6351002019693018, 0.4435211436398484, 0.8068844348124993,
                0.8926848452848529, 0.8081301250035834, 0.25490020128427027, 0.08395441205038512, 0.13853413517651525,
                0.4317280885585699 };
        double actual[] = new double[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextDouble();
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testFloat() {
        float expected[] = { 0.9206826283274985f, 0.6351002019693018f, 0.4435211436398484f, 0.8068844348124993f,
                0.8926848452848529f, 0.8081301250035834f, 0.25490020128427027f, 0.08395441205038512f,
                0.13853413517651525f, 0.4317280885585699f };
        float actual[] = new float[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextFloat();
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testBoolean() {
        boolean expected[] = { true, true, true, false, false, false, true, true, true, true };
        boolean actual[] = new boolean[expected.length];
        for (int i = 0; i < actual.length; i++) {
            // bool(rndgen.getrandbits(1))
            actual[i] = r.nextBoolean();
        }
        assertThat(actual, equalTo(expected));
    }
}
