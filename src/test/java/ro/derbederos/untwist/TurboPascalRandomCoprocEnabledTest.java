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

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;

public class TurboPascalRandomCoprocEnabledTest extends ReverseBitsStreamGeneratorAbstractTest<TurboPascalRandom> {

    @Override
    protected TurboPascalRandom makeGenerator() {
        return new TurboPascalRandom(0xC44002DC, true);
    }

    @Test
    public void testZero() {
        //-1498392781 seed was causing the pascal generator to generate 1.0 in turbo pascal 5 and 6.
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, true);
        assertThat(generator.nextDouble(), equalTo(0.0));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {12, 8, 11, 13, 12, 7, 3, 15, 2, 3};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {-845578675, -2088293502, -1339891317, -783150152, -964345191,
                1941516542, 936439031, -251921196, 703152165, 1070159034};
        int[] actual = generateIntArray(expected.length, (IntSupplier) generator::nextInt);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {2L, 8L, 14L, 4L, 10L, 0L, 6L, 12L, 2L, 8L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-3631732753113339006L, -5754789383197551688L, -4141831055458356994L, 4021975016885976276L,
                3020015553856754874L, -8904021482262177232L, -1975669623462997322L, 6926665487186659276L,
                5403570775623280370L, -4166240069301493336L};
        long[] actual = generateLongArray(expected.length, (LongSupplier) generator::nextLong);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.30312337283976376, 0.013781279791146517, 0.18803224223665893, 0.31765864603221416,
                0.27547088847495615, 0.9520445461384952, 0.718031702330336, 0.44134502578526735,
                0.6637153711635619, 0.7491658166982234};
        double[] actual = generateDoubleArray(expected.length, generator::nextDouble);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.30312338F, 0.013781279F, 0.18803224F, 0.31765863F, 0.27547088F,
                0.95204455F, 0.7180317F, 0.44134504F, 0.66371536F, 0.74916583F};
        float[] actual = generateFloatArray(expected.length, generator::nextFloat);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {true, true, true, true, true, false, false, true, false, false,
                true, false, true, true, false, true, false, true, true, false};
        boolean[] actual = generateBooleanArray(expected.length, generator::nextBoolean);

        assertThat(actual, equalTo(expected));
    }

    @Test
    @Ignore
    public void testEnabledVsDisabled() {
        TurboPascalRandom r1 = new TurboPascalRandom(1);
        TurboPascalRandom r2 = new TurboPascalRandom(1, true);
        Matcher<Double> equalToMatcher = equalTo(0.5d);
        for (long i = 0; i < 1L << 32; i++) {
            assertThat(Math.abs(r1.nextDouble() - r2.nextDouble()), equalToMatcher);
        }
    }
}
