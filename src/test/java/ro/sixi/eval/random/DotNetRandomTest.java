package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static ro.sixi.eval.random.Utils.between;
import static ro.sixi.eval.random.Utils.betweenPredicate;
import static ro.sixi.eval.random.Utils.createStream;
import static ro.sixi.eval.random.Utils.toByteList;
import static ro.sixi.eval.util.ArrayUtils.generateBooleanArray;
import static ro.sixi.eval.util.ArrayUtils.generateDoubleArray;
import static ro.sixi.eval.util.ArrayUtils.generateIntArray;
import static ro.sixi.eval.util.ArrayUtils.generateLongArray;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DotNetRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private DotNetRandom r;

    @Before
    public void setup() {
        r = new DotNetRandom(1234567890L);
    }

    @Test
    public void testRandomCtor1_PosTest1() {
        DotNetRandom r = new DotNetRandom();
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest1() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE);
        DotNetRandom r = new DotNetRandom(randValue);
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest2() {
        DotNetRandom r = new DotNetRandom(Integer.MIN_VALUE);
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomNext1_PosTest1() {
        DotNetRandom r = new DotNetRandom(-55);
        int value = r.nextInt();
        assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest2() {
        DotNetRandom r = new DotNetRandom(Integer.MAX_VALUE);
        int value = r.nextInt();
        assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest3() {
        DotNetRandom r = new DotNetRandom(0);
        int value = r.nextInt();
        assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        DotNetRandom r = new DotNetRandom(randValue);
        int value = r.nextInt();
        assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext2_int_PosTest1() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(-55);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest2() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(Integer.MAX_VALUE);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest3() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(0);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(randValue);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest5() {
        int maxValue = newInt32WithCondition(-55, (v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(maxValue);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest6() {
        DotNetRandom random = new DotNetRandom(0);
        int value = random.nextInt(0);
        assertThat(value, equalTo(0));
    }

    @Test
    public void testRandomNext3_int_int_PosTest1() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom();
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest2() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(Integer.MAX_VALUE);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest3() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(0);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(randValue);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest5() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(maxValue);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest6() {
        assertThat(new DotNetRandom(0).nextInt(0, 0), equalTo(0));
        assertThat(new DotNetRandom(0).nextInt(Integer.MAX_VALUE, Integer.MAX_VALUE), equalTo(Integer.MAX_VALUE));
        assertThat(new DotNetRandom(0).nextInt(Integer.MIN_VALUE, Integer.MIN_VALUE), equalTo(Integer.MIN_VALUE));
        assertThat(new DotNetRandom(0).nextInt(Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
                equalTo(Integer.MAX_VALUE - 1));
        assertThat(new DotNetRandom(Integer.MAX_VALUE).nextInt(Integer.MAX_VALUE - 1, Integer.MAX_VALUE),
                equalTo(Integer.MAX_VALUE - 1));
    }

    @Test
    public void testRandomNext3_int_int_PosTest7() {
        int maxValue = newInt32WithCondition(-55, (v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition(-55, (v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(maxValue);
        int value = random.nextInt(minValue, minValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNextDouble_PosTest1() {
        assertThat(new DotNetRandom(-55).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest2() {
        assertThat(new DotNetRandom(Integer.MAX_VALUE).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest3() {
        assertThat(new DotNetRandom(0).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest4() {
        int randValue = newInt32WithCondition(-55, (v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        assertThat(new DotNetRandom(randValue).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextBytes_PosTest1() {
        byte b[] = new byte[1024];
        new DotNetRandom(-55).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(0).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Integer.MAX_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(-1).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Byte.MAX_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Byte.MIN_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testRandomNextBytes_PosTest2() {
        byte b[] = new byte[1];
        new DotNetRandom(-55).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(0).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Integer.MAX_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(-1).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Byte.MAX_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new DotNetRandom(Byte.MIN_VALUE).nextBytes(b);
        assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testIntNegativeValue() {
        expectedException.expect(IllegalArgumentException.class);

        new DotNetRandom(1234567890).nextInt(-16);
    }

    @Test
    public void test16() {
        int[] expected = { 8, 6, 4, 0, 5, 13, 10, 1, 12, 13 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testInt32bitRange() {
        int[] expected = { -287579909, 90175452, 80605103, 1593771972, 1778445194, -482557609, 1894541034, 1056929146,
                779980809, 1253822814, 1884515393, 614983788, -358924531, 298830117, 903849615, -549623606, 676576329,
                853008319, 370052958, 194295684 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(-1_000_000_000, Integer.MAX_VALUE));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void test9() {
        int[] expected = { 4, 3, 2, 0, 2, 7, 5, 0, 6, 7, 6, 6, 7, 4, 2, 2, 1, 8, 3, 8 };
        int[] actual = generateIntArray(expected.length, () -> r.nextInt(9));

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testLong() {
        long[] expected = { -659861015L, -2890171289807960499L, -1441396374L, 7740928225207699870L,
                1208723606135141233L, 7682170035453475444L, -375136752L, -1532912740L, 602691552480251525L,
                -517781486L };
        long[] actual = generateLongArray(expected.length, () -> r.nextLong());

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testDouble() {
        double[] expected = { 0.547308153727701, 0.42220238895258044, 0.3072717289008534, 0.006450907330238682,
                0.31335299849200665, 0.8306209607192413, 0.6481559642814826, 0.07130287451264582, 0.7655025449420803,
                0.8250625430723012 };
        double[] actual = generateDoubleArray(expected.length, () -> r.nextDouble());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testFloat() {
        expectedException.expect(UnsupportedOperationException.class);
        new DotNetRandom(1234567890).nextFloat();
    }

    @Test
    public void testBoolean() {
        boolean[] expected = { true, false, false, false, false, true, true, false, true, true, true, true, true, true,
                false, false, false, true, false, true };
        boolean[] actual = generateBooleanArray(expected.length, () -> r.nextBoolean());

        assertThat(actual, equalTo(expected));
    }

    @Test
    @Ignore
    public void testIntNoLimitStream() {
        DotNetRandom r = new DotNetRandom(1234567890);
        IntPredicate betweenPredicate = betweenPredicate(0, Integer.MAX_VALUE);
        boolean result = createStream(1_000_000_000L, () -> r.nextInt()).allMatch(betweenPredicate);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testIntStream() {
        DotNetRandom r = new DotNetRandom(rand.nextInt());
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> r.nextInt(1000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testRangeIntStream() {
        DotNetRandom r = new DotNetRandom(rand.nextInt());
        final Matcher<Integer> betweenMatcher = between(-1000, 3000);
        createStream(100000, () -> r.nextInt(-1000, 3000)).forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testDoubleStream() {
        DotNetRandom r = new DotNetRandom(rand.nextInt());
        final Matcher<Double> betweenMatcher = between(0d, 1d);
        createStream(100000, () -> r.nextDouble()).forEach((t) -> assertThat(t, betweenMatcher));
    }

    private static Integer seed = null;
    private static DotNetRandom rand;

    private void setSeed(Integer seed) {
        if (DotNetRandomTest.seed == null) {
            DotNetRandomTest.seed = seed;
            if (seed != null) {
                rand = new DotNetRandom(DotNetRandomTest.seed);
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
