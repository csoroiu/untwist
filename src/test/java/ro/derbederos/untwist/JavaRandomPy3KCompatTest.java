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

import org.junit.Before;
import org.junit.Test;

import java.util.stream.DoubleStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.RandomUtils.nextInts;

public class JavaRandomPy3KCompatTest {
    // openjdk random tests.
    // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/test/java/util/Random

    private JavaRandomPy3KCompat generator;

    @Before
    public void setup() {
        generator = new JavaRandomPy3KCompat(0L);
    }

    @Test
    public void testSet32BitSeedIntVsLong() {
        final int seedInt = 0x12345678;
        final long seedLong = 0x12345678L;

        JavaRandomPy3KCompat rInt = new JavaRandomPy3KCompat();
        rInt.setSeed(seedInt);
        int[] actualInt = nextInts(rInt, 10, 0, 1000).toArray();

        JavaRandomPy3KCompat rLong = new JavaRandomPy3KCompat();
        rLong.setSeed(seedLong);
        int[] actualLong = nextInts(rLong, 10, 0, 1000).toArray();

        assertThat("IntVsLong", actualInt, equalTo(actualLong));
    }

    @Test
    public void testSetSeedLong() {
        generator.setSeed(42523532L);
        int expected = -1778905166;
        int actual = generator.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBytes() {
        byte[] expected = new byte[]{96, -76, 32, -69, 56, 81, -39, -44};
        byte[] actual = new byte[expected.length];
        generator.nextBytes(actual);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_NoBound() {
        int expected = -1155484576;
        int actual = generator.nextInt();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_Lue() {
        int expected = 12;
        int actual = generator.nextInt(42);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_Pow2() {
        int expected = 784870680;
        int actual = generator.nextInt(1 << 30);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextInt_IntOverflow() {
        generator.setSeed(215660466117472L);
        int expected = 4224;
        int actual = generator.nextInt(100000);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextLongExactValue() {
        long expected = -4962768465676381896L;
        long actual = generator.nextLong();
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.730967787376657, 0.24053641567148587, 0.6374174253501083, 0.5504370051176339,
                0.5975452777972018, 0.3332183994766498, 0.3851891847407185, 0.984841540199809,
                0.8791825178724801, 0.9412491794821144};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextFloatExactValue() {
        double[] expected = {0.7309677600860596, 0.2405364215373993, 0.6374174356460571, 0.5504370331764221,
                0.5975452661514282, 0.33321839570999146, 0.3851891756057739, 0.984841525554657,
                0.8791825175285339, 0.9412491917610168};
        double[] actual = DoubleStream.generate(generator::nextFloat).limit(expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testNextBooleanExactValue() {
        boolean actual = generator.nextBoolean();
        assertThat(actual, equalTo(true));
    }
}
