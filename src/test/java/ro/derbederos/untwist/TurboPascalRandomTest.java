/*
 * Copyright (c) 2017-2018 Claudiu Soroiu
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

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.Utils.nextBooleans;
import static ro.derbederos.untwist.Utils.nextFloats;

public class TurboPascalRandomTest extends ReverseBitsStreamGeneratorAbstractTest<TurboPascalRandom> {

    @Override
    protected TurboPascalRandom makeGenerator() {
        return new TurboPascalRandom(0xC44002DC);
    }

    @Test
    public void testZero() {
        //-1498392781 seed was causing the pascal generator to generate 1.0 in turbo pascal 5 and 6.
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, false);
        assertThat(generator.nextDouble(), equalTo(0.5));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {12, 8, 11, 13, 12, 7, 3, 15, 2, 3};
        int[] actual = nextInts(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {-845578675, -2088293502, -1339891317, -783150152, -964345191,
                1941516542, 936439031, -251921196, 703152165, 1070159034};
        int[] actual = nextInts(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntWideRangeExactValue() {
        int[] expected = {1527817682, 617118176, 1165570231, 1573567217, 1440781940, 422802816, -313748783,
                1962868074, -484708547, -215754667, 628231149, -442848315, 1810384166, 1901073709,
                181865279, 1381745985, -78013412, 1258659168, 1436617142, -848949435};
        int[] actual = nextInts(generator, expected.length, -1_000_000_000, Integer.MAX_VALUE).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {2L, 8L, 14L, 4L, 10L, 0L, 6L, 12L, 2L, 8L};
        long[] actual = nextLongs(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-3631732753113339006L, -5754789383197551688L, -4141831055458356994L, 4021975016885976276L,
                3020015553856754874L, -8904021482262177232L, -1975669623462997322L, 6926665487186659276L,
                5403570775623280370L, -4166240069301493336L};
        long[] actual = nextLongs(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.8031233728397638, 0.5137812797911465, 0.6880322422366589, 0.8176586460322142,
                0.7754708884749562, 0.4520445461384952, 0.21803170233033597, 0.9413450257852674,
                0.16371537116356194, 0.24916581669822335};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        Float[] expected = {0.80312335F, 0.5137813F, 0.68803227F, 0.81765866F, 0.7754709F,
                0.45204455F, 0.2180317F, 0.94134504F, 0.16371538F, 0.24916582F};
        Float[] actual = nextFloats(generator, expected.length).toArray(Float[]::new);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        Boolean[] expected = {true, true, true, true, true, false, false, true, false, false,
                true, false, true, true, false, true, false, true, true, false};
        Boolean[] actual = nextBooleans(generator, expected.length).toArray(Boolean[]::new);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCalls() {
        super.testNextPrevMixedCalls();
    }
}
