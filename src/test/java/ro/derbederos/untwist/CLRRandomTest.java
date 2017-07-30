package ro.derbederos.untwist;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static ro.derbederos.untwist.Utils.createStream;

public class CLRRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CLRRandom r;

    @Before
    public void setup() {
        r = new CLRRandom(1234567890L);
    }

    @Test
    public void testRandomCtor1_PosTest1() {
        CLRRandom r = new CLRRandom();
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest1() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE);
        CLRRandom r = new CLRRandom(randValue);
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest2() {
        CLRRandom r = new CLRRandom(Integer.MIN_VALUE);
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomNext1_PosTest1() {
        CLRRandom r = new CLRRandom(-55);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, Utils.between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest2() {
        CLRRandom r = new CLRRandom(Integer.MAX_VALUE);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, Utils.between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest3() {
        CLRRandom r = new CLRRandom(0);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, Utils.between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        CLRRandom r = new CLRRandom(randValue);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, Utils.between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext2_int_PosTest1() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(-55);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, Utils.between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest2() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(Integer.MAX_VALUE);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, Utils.between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest3() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(0);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, Utils.between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(randValue);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, Utils.between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest5() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, Utils.between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest6() {
        CLRRandom random = new CLRRandom(0);
        int value = random.nextInt(0);
        assertThat(value, equalTo(0));
    }

    @Test
    public void testRandomNext2_int_NegTest1() {
        expectedException.expect(IllegalArgumentException.class);
        CLRRandom random = new CLRRandom(-55);
        random.nextInt(-1);
    }

    @Test
    public void testRandomNext3_int_int_PosTest1() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom();
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest2() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(Integer.MAX_VALUE);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest3() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(0);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(randValue);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest5() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest6() {
        assertThat(new CLRRandom(0).nextInt(0, 0), equalTo(0));
        assertThat(new CLRRandom(0).nextInt(Integer.MAX_VALUE, Integer.MAX_VALUE), equalTo(Integer.MAX_VALUE));
        assertThat(new CLRRandom(0).nextInt(Integer.MIN_VALUE, Integer.MIN_VALUE), equalTo(Integer.MIN_VALUE));
        assertThat(new CLRRandom(0).nextInt(Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
                equalTo(Integer.MAX_VALUE - 1));
        assertThat(new CLRRandom(Integer.MAX_VALUE).nextInt(Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
                equalTo(Integer.MAX_VALUE - 1));
    }

    @Test
    public void testRandomNext3_int_int_PosTest7() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(minValue, minValue);
        MatcherAssert.assertThat(value, Utils.between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_NegTest1() {
        expectedException.expect(IllegalArgumentException.class);
        CLRRandom random = new CLRRandom(-55);
        random.nextInt(1, 0);
    }

    @Test
    public void testRandomNextDouble_PosTest1() {
        MatcherAssert.assertThat(new CLRRandom(-55).nextDouble(), Utils.between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest2() {
        MatcherAssert.assertThat(new CLRRandom(Integer.MAX_VALUE).nextDouble(), Utils.between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest3() {
        MatcherAssert.assertThat(new CLRRandom(0).nextDouble(), Utils.between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        MatcherAssert.assertThat(new CLRRandom(randValue).nextDouble(), Utils.between(0d, 1d));
    }

    @Test
    public void testRandomNextBytes_PosTest1() {
        byte[] b = new byte[1024];
        new CLRRandom(-55).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(0).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Integer.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(-1).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MIN_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testRandomNextBytes_PosTest2() {
        byte[] b = new byte[1];
        new CLRRandom(-55).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(0).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Integer.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(-1).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MIN_VALUE).nextBytes(b);
        MatcherAssert.assertThat(Utils.toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testRandomNextBytes_NegTest1() {
        expectedException.expect(NullPointerException.class);
        CLRRandom random = new CLRRandom(-55);
        random.nextBytes(null);
    }

    @Test
    public void testSet32BitSeedIntVsLongVsArray() {
        final int seedInt = 0x12345678;
        final long seedLong = 0x12345678L;
        final int[] seedArray = {seedInt};
        final int[] expected = {853, 486, 124, 219, 890, 790, 885, 574, 751, 165};

        CLRRandom rInt = new CLRRandom(seedInt);
        int[] actualInt = ArrayUtils.generateIntArray(expected.length, () -> rInt.nextInt(1000));

        CLRRandom rLong = new CLRRandom(seedLong);
        int[] actualLong = ArrayUtils.generateIntArray(expected.length, () -> rLong.nextInt(1000));

        CLRRandom rArray = new CLRRandom(seedArray);
        int[] actualArray = ArrayUtils.generateIntArray(expected.length, () -> rArray.nextInt(1000));

        assertThat(actualInt, equalTo(expected));
        assertThat(actualLong, equalTo(expected));
        assertThat(actualArray, equalTo(expected));
    }

    @Test
    public void testNextInt_NegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        r.nextInt(-16);
    }

    @Test
    public void testNextInt_16() {
        int[] expected = {8, 6, 4, 0, 5, 13, 10, 1, 12, 13};
        int[] actual = ArrayUtils.generateIntArray(expected.length, () -> r.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_32bit() {
        int[] expected = {-287579909, 90175452, 80605103, 1593771972, 1778445194, -482557609, 1894541034, 1056929146,
                779980809, 1253822814, 1884515393, 614983788, -358924531, 298830117, 903849615, -549623606, 676576329,
                853008319, 370052958, 194295684};
        int[] actual = ArrayUtils.generateIntArray(expected.length, () -> r.nextInt(-1_000_000_000, Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_9() {
        int[] expected = {4, 3, 2, 0, 2, 7, 5, 0, 6, 7, 6, 6, 7, 4, 2, 2, 1, 8, 3, 8};
        int[] actual = ArrayUtils.generateIntArray(expected.length, () -> r.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLong() {
        long[] expected = {-659861015L, -2890171289807960499L, -1441396374L, 7740928225207699870L,
                1208723606135141233L, 7682170035453475444L, -375136752L, -1532912740L, 602691552480251525L, -517781486L};
        long[] actual = ArrayUtils.generateLongArray(expected.length, () -> r.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDouble() {
        double[] expected = {0.547308153727701, 0.42220238895258044, 0.3072717289008534, 0.006450907330238682,
                0.31335299849200665, 0.8306209607192413, 0.6481559642814826, 0.07130287451264582, 0.7655025449420803,
                0.8250625430723012};
        double[] actual = ArrayUtils.generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testNextFloat() {
        float[] expected = {0.54730815F, 0.42220238F, 0.30727172F, 0.0064509073F, 0.313353F, 0.83062094F, 0.648156F,
                0.071302876F, 0.7655026F, 0.8250625F};
        float[] actual = ArrayUtils.generateFloatArray(expected.length, () -> r.nextFloat());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBoolean() {
        boolean[] expected = {true, false, false, false, false, true, true, false, true, true, true, true, true, true,
                false, false, false, true, false, true};
        boolean[] actual = ArrayUtils.generateBooleanArray(expected.length, () -> r.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @Ignore
    public void testNextInt_NoBoundStream() {
        IntPredicate betweenPredicate = Utils.betweenPredicate(0, Integer.MAX_VALUE);
        boolean result = Utils.createStream(1_000_000_000L, () -> r.nextInt()).allMatch(betweenPredicate);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextIntStream() {
        CLRRandom r = new CLRRandom(rand.nextInt());
        final Matcher<Integer> betweenMatcher = Utils.between(0, 1000);
        Utils.createStream(100000, () -> r.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextInt_RangeStream() {
        CLRRandom r = new CLRRandom(rand.nextInt());
        final Matcher<Integer> betweenMatcher = Utils.between(-1000, 3000);
        Utils.createStream(100000, () -> r.nextInt(-1000, 3000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        r.nextInt(200, 100);
    }

    @Test
    public void testNextDoubleStream() {
        CLRRandom r = new CLRRandom(rand.nextInt());
        final Matcher<Double> betweenMatcher = Utils.between(0d, 1d);
        Utils.createStream(100000, r::nextDouble).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextBytes_NullBuffer() {
        expectedException.expect(NullPointerException.class);

        r.nextBytes(null);
    }

    private static Integer seed = null;
    private static CLRRandom rand;

    private void setSeed(Integer seed) {
        if (CLRRandomTest.seed == null) {
            CLRRandomTest.seed = seed;
            if (seed != null) {
                rand = new CLRRandom(CLRRandomTest.seed);
            }
        }
    }

    private int newInt32WithCondition(Integer seed, Predicate<Integer> loopCondition) {
        int result;
        do {
            setSeed(seed);
            result = rand.nextInt();
        } while (loopCondition.test(result));
        return result;
    }
}
