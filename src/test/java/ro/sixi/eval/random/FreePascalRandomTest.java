package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FreePascalRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private FreePascalRandom r;

    @Before
    public void setup() {
        r = new FreePascalRandom(1234567890L);
    }

    @Test
    public void testSetSeedLongVsSetSeedArray() {
        final long seedLong = 0x0304050601010102L;
        final int[] seedArray = { 0x01010102, 0x03040506 };
        final int expected[] = { 4, 796, 258, 748, 665, 390, 89, 21, 167, 410 };

        r.setSeed(seedLong);
        int actualLong[] = new int[expected.length];
        for (int i = 0; i < actualLong.length; i++) {
            actualLong[i] = r.nextInt(1000);
        }

        r.setSeed(seedArray);
        int actualArray[] = new int[expected.length];
        for (int i = 0; i < actualLong.length; i++) {
            actualArray[i] = r.nextInt(1000);
        }
        assertThat(actualLong, equalTo(expected));
        assertThat(actualArray, equalTo(expected));
    }

    @Test
    public void testIntNegativeValue() {
        int expected[] = { -10, -6, -9, -3, -14, -4, -14, -7, -7, -13, -12, -3, -5, -3, -15 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(-16);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testIntMaxValue() {
        int expected[] = { 1328851648, 731237375, 1270502066, 320041495, 1908433477, 499156889, 1914814095, 927307221,
                982618676, 1814042781 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(Integer.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongIntMaxValue() {
        long expected[] = { 1287685506L, 1673686469L, 1518527220L, 1096406138L, 631473891L, 504365730L, 534922973L,
                63348502L, 983415046L, 1939773091L };
        long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(Integer.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLongLongMaxValue() {
        final long expected[] = { 6281281229428446594L, 2749135515611866470L, 4287725035768038540L,
                7965508383203884321L, 6359136809580176489L, 3128970990868452750L, 3297195868290960004L,
                171953701487956256L, 1742057592483719353L, 3819555233715651620L };
        final long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(Long.MAX_VALUE);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong32bit() {
        long expected[] = { 2657703298L, 2541004134L, 3816866956L, 3829628193L, 1965237353L, 3342292366L, 1147030148L,
                4278243616L, 2319689913L, 2308637732L };
        long actual[] = new long[expected.length];
        final long _32bit = 1L << 32;
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(_32bit);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test16() {
        int expected[] = { 9, 5, 9, 2, 14, 3, 14, 6, 7, 13 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(16);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong16() {
        long expected[] = { 2, 6, 12, 1, 9, 14, 4, 0, 9, 4 };
        long actual[] = new long[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextLong(16);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test9() {
        int expected[] = { 5, 3, 5, 1, 7, 2, 8, 3, 4, 7 };
        int actual[] = new int[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextInt(9);
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDouble() {
        double expected[] = { 0.6187947695143521, 0.3405089376028627, 0.5916236280463636, 0.14903093478642404,
                0.8886835901066661, 0.23243803973309696, 0.8916547971311957, 0.4318110744934529, 0.457567477831617,
                0.8447294970974326 };
        double actual[] = new double[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextDouble();
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testFloat() {
        float expected[] = { 0.6187947989f, 0.3405089378f, 0.5916236043f, 0.1490309387f, 0.8886836171f, 0.2324380428f,
                0.8916547894f, 0.4318110645f, 0.4575674832f, 0.8447294831f };
        float actual[] = new float[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextFloat();
        }
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testBoolean() {
        boolean expected[] = { true, false, true, false, true, false, true, false, false, true };
        boolean actual[] = new boolean[expected.length];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = r.nextBoolean();
        }
        assertThat(actual, equalTo(expected));
    }

}
