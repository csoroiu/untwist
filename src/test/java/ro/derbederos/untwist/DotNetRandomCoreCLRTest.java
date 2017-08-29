/*
 * Copyright (c) 2017 Claudiu Soroiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.derbederos.untwist;

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
 * <p>
 * <a href="https://github.com/dotnet/coreclr/tree/master/tests/src/CoreMangLib/cti/system/random">
 * .NET Core Random class tests</a>
 */
public class DotNetRandomCoreCLRTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final DotNetRandom rand = new DotNetRandom(-55);

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
        DotNetRandom r = new DotNetRandom();
        assertThat(r, notNullValue());
    }

    @Test
    public void testRandomCtor2_int_PosTest1() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE);
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
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        DotNetRandom r = new DotNetRandom(randValue);
        int value = r.nextInt();
        assertThat(value, between(0, Integer.MAX_VALUE));
    }

    @Test
    public void testRandomNext2_int_PosTest1() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(-55);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest2() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(Integer.MAX_VALUE);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest3() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(0);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
        DotNetRandom random = new DotNetRandom(randValue);
        int value = random.nextInt(maxValue);
        assertThat(value, between(0, maxValue));
    }

    @Test
    public void testRandomNext2_int_PosTest5() {
        int maxValue = newInt32WithCondition((v) -> v <= 0 || v == Integer.MAX_VALUE);
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
    public void testRandomNext2_int_NegTest1() {
        expectedException.expect(IllegalArgumentException.class);
        DotNetRandom random = new DotNetRandom(-55);
        random.nextInt(-1);
    }

    @Test
    public void testRandomNext3_int_int_PosTest1() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom();
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest2() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(Integer.MAX_VALUE);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest3() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(0);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest4() {
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(randValue);
        int value = random.nextInt(minValue, maxValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_PosTest5() {
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
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
        int maxValue = newInt32WithCondition((v) -> v == Integer.MAX_VALUE);
        int minValue = newInt32WithCondition((v) -> v >= maxValue);
        DotNetRandom random = new DotNetRandom(maxValue);
        int value = random.nextInt(minValue, minValue);
        assertThat(value, between(minValue, maxValue));
    }

    @Test
    public void testRandomNext3_int_int_NegTest1() {
        expectedException.expect(IllegalArgumentException.class);
        DotNetRandom random = new DotNetRandom(-55);
        random.nextInt(1, 0);
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
        int randValue = newInt32WithCondition((v) -> v == Integer.MIN_VALUE || v == 0);
        if (randValue > 0) {
            randValue *= -1;
        }
        assertThat(new DotNetRandom(randValue).nextDouble(), between(0d, 1d));
    }

    @Test
    public void testRandomNextBytes_PosTest1() {
        byte[] b = new byte[1024];
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
        byte[] b = new byte[1];
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
    public void testRandomNextBytes_NegTest1() {
        expectedException.expect(NullPointerException.class);
        DotNetRandom random = new DotNetRandom(-55);
        random.nextBytes(null);
    }
}
