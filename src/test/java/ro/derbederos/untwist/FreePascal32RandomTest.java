/*
 * Copyright (c) 2017-2019 Claudiu Soroiu
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
import static ro.derbederos.untwist.RandomUtils.nextInts;
import static ro.derbederos.untwist.RandomUtils.nextLongs;

public class FreePascal32RandomTest extends FreePascalRandomTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new FreePascal32Random(123456789013L);
    }

    @Override
    @Test
    public void testNextLongNeg() {
        long nextLong = generator.nextLong(-16);
        assertThat(nextLong, equalTo(-7L));
    }

    @Override
    @Test
    public void testPrevLongNeg() {
        long prevLong = generator.prevLong(-16);
        assertThat(prevLong, equalTo(-2L));
    }

    @Test
    public void testNextInt_NegativeWideRangeExactValue() {
        int[] expected = {-11004583, 42205604, -654991934, -952758273, -420551746, 396935981, -231068866,
                -154686751, 968007087, -1393527701, -428441773, 1341448537, 218004845, -803499574,
                -288577197, -257747525, -1205544825, 1463777007, 223753217, -1413317131, -132203888,
                702835146, 763536774, -417304623, 1237982790, 526775294, -499874905, 1319803476};
        int[] actual = nextInts(generator, expected.length, 1_500_000_000, -1_500_000_000).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntWideRangeExactValue() {
        int[] expected = {562196242, 618022306, -113450310, -425855205, 132515243, 990191660, 331313331,
                411450484, 1589337315, -888293393, 124237333, 1981137602, 802464052, -269258766,
                270977820, 303323116, -691069050, 2109479888, 808495021, -909055696};
        int[] actual = nextInts(generator, expected.length, -1_000_000_000, Integer.MAX_VALUE).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {7L, 8L, 4L, 2L, 5L, 10L, 6L, 7L, 13L, 0L, 5L, 15L, 9L, 3L};
        long[] actual = nextLongs(generator, expected.length, 0, 16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLongNeg16ExactValue() {
        long[] expected = {-7L, -7L, -4L, -2L, -5L, -9L, -6L, -6L, -12L, 0L, -5L, -14L, -8L, -3L};
        long[] actual = nextLongs(generator, expected.length, 0, -16).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testPrevLong() {
        long expected = generator.nextLong(0x7ABCDEF8FFFFFFFFL);
        long actual = generator.prevLong(0x7ABCDEF8FFFFFFFFL);

        assertThat(expected, equalTo(4766053535330551952L));
        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    @Ignore
    public void testNextPrevMixedCalls() {
        super.testNextPrevMixedCalls();
    }
}
