package ro.derbederos.untwist;

import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.toByteList;

/**
 * This class contains the tests from the CoreCLR library.
 * https://github.com/dotnet/coreclr/tree/master/tests/src/CoreMangLib/cti/system/random
 */
public class CLRRandomCoreCLRTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static CLRRandom rand = new CLRRandom(-55);

    // https://github.com/dotnet/coreclr/blob/master/tests/src/Common/CoreCLRTestLibrary/Generator.cs
    private int newInt32WithCondition(Predicate<Integer> loopCondition) {
        int result;
        do {
            result = rand.nextInt();
        } while (loopCondition.test(result));
        return result;
    }

    @Test
    public void testRandomCtor1_PosTest1() {
        CLRRandom r = new CLRRandom();
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest1() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE);
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
        MatcherAssert.assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest2() {
        CLRRandom r = new CLRRandom(Integer.MAX_VALUE);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest3() {
        CLRRandom r = new CLRRandom(0);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext1_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        CLRRandom r = new CLRRandom(randValue);
        int value = r.nextInt();
        MatcherAssert.assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext2_int_PosTest1() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(-55);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest2() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(Integer.MAX_VALUE);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest3() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(0);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(randValue);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest5() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(maxValue);
        MatcherAssert.assertThat(value, between(0, maxValue));
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
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom();
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest2() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(Integer.MAX_VALUE);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest3() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(0);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(randValue);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest5() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(minValue, maxValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
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
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        CLRRandom random = new CLRRandom(maxValue);
        int value = random.nextInt(minValue, minValue);
        MatcherAssert.assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_NegTest1() {
        expectedException.expect(IllegalArgumentException.class);
        CLRRandom random = new CLRRandom(-55);
        random.nextInt(1, 0);
    }

    @Test
    public void testRandomNextDouble_PosTest1() {
        MatcherAssert.assertThat(new CLRRandom(-55).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest2() {
        MatcherAssert.assertThat(new CLRRandom(Integer.MAX_VALUE).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest3() {
        MatcherAssert.assertThat(new CLRRandom(0).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextDouble_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        MatcherAssert.assertThat(new CLRRandom(randValue).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextBytes_PosTest1() {
        byte[] b = new byte[1024];
        new CLRRandom(-55).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(0).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Integer.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(-1).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MIN_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testRandomNextBytes_PosTest2() {
        byte[] b = new byte[1];
        new CLRRandom(-55).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(0).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Integer.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(-1).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MAX_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
        new CLRRandom(Byte.MIN_VALUE).nextBytes(b);
        MatcherAssert.assertThat(toByteList(b), everyItem(greaterThanOrEqualTo((byte) -128)));
    }

    @Test
    public void testRandomNextBytes_NegTest1() {
        expectedException.expect(NullPointerException.class);
        CLRRandom random = new CLRRandom(-55);
        random.nextBytes(null);
    }
}
