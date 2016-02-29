package ro.sixi.eval.random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.everyItem;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ro.sixi.eval.util.ArrayUtils;

public class DotNetRandomTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
    @SuppressWarnings("deprecation")
    public void testFloat() {
        expectedException.expect(UnsupportedOperationException.class);
        new DotNetRandom(1234567890).nextFloat();
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

    private static List<Byte> toByteList(byte[] b) {
        return Arrays.asList(ArrayUtils.generateArray(Byte[]::new, b.length, (i) -> Byte.valueOf(b[i])));
    }

    public <T extends java.lang.Comparable<T>> Matcher<T> between(T minValue, T maxValue) {
        return allOf(greaterThanOrEqualTo(minValue), lessThan(maxValue));
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
