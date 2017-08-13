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

import org.junit.Test;

import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.*;


public class ReverseBitsStreamGeneratorTest extends ReverseBitsStreamGeneratorAbstractTest<ReverseBitsStreamGenerator> {

    @Override
    protected ReverseBitsStreamGenerator makeGenerator() {
        ReverseBitsStreamGenerator generator = new TestBitStreamGenerator();
        generator.setSeed(1000);
        return generator;
    }

    static class BitRandom extends ReversibleJdkRandom {
        private static final long serialVersionUID = 1L;

        public BitRandom() {
        }

        int nextBits(int bits) {
            return this.next(bits);
        }

        int prevBits(int bits) {
            return this.prev(bits);
        }
    }

    static class TestBitStreamGenerator extends ReverseBitsStreamGenerator {
        private static final long serialVersionUID = 1L;
        private final BitRandom ran = new BitRandom();

        TestBitStreamGenerator() {
        }

        @Override
        public void setSeed(int seed) {
            this.ran.setSeed((long) seed);
            this.clear();
        }

        @Override
        public void setSeed(int[] seed) {
            this.ran.setSeed(RandomUtils.convertToLong(seed));
            this.clear();
        }

        @Override
        public void setSeed(long seed) {
            this.ran.setSeed(seed);
        }

        @Override
        protected int next(int bits) {
            return this.ran.nextBits(bits);
        }

        @Override
        protected int prev(int bits) {
            return this.ran.prevBits(bits);
        }
    }

    @Test
    public void testPrevLong() {
        long expected = generator.nextLong(0x7ABCDEF8FFFFFFFFL);
        long actual = generator.prevLong(0x7ABCDEF8FFFFFFFFL);

        assertThat(expected, equalTo(6550299665512127023L));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt() {
        int expected = generator.nextInt(100);
        int actual = generator.prevInt(100);

        assertThat(expected, equalTo(87));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt2N() {
        int expected = generator.nextInt(2 << 19);
        int actual = generator.prevInt(2 << 19);

        assertThat(expected, equalTo(744682));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_NoBound() {
        int expected = generator.nextInt();
        int actual = generator.prevInt();

        assertThat(expected, equalTo(-1244746321));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevInt_overflow() {
        generator.setSeed(215660466117472L);
        int expected = generator.nextInt(100000);
        int actual = generator.prevInt(100000);
        int actualReverse = generator.prevInt(100000);
        int expectedReverse = generator.nextInt(100000);

        assertThat(expected, equalTo(4224));
        assertThat(actual, equalTo(expected));
        assertThat(actualReverse, equalTo(65354));
        assertThat(actualReverse, equalTo(expectedReverse));
    }

    @Test
    public void testPrevLong_NoBound() {
        long expected = generator.nextLong();
        long actual = generator.prevLong();

        assertThat(expected, equalTo(-5346144739450824145L));
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testPrevBytes() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53, 63};
        byte[] prevBytes = new byte[]{63, 53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Test
    public void testPrevBytes_not32bitmultiple() {
        byte[] nextBytes = new byte[]{-81, -83, -50, -75, 47, -38, 53};
        byte[] prevBytes = new byte[]{53, -38, 47, -75, -50, -83, -81};
        byte[] expected = new byte[nextBytes.length];
        byte[] actual = new byte[prevBytes.length];

        generator.nextBytes(expected);
        generator.prevBytes(actual);

        assertThat(expected, equalTo(nextBytes));
        assertThat(actual, equalTo(prevBytes));
    }

    @Override
    @Test
    public void testNextInt16ExactValue() {
        int[] expected = {11, 3, 9, 7, 15, 0, 0, 13, 7, 5};
        int[] actual = generateIntArray(expected.length, () -> generator.nextInt(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextIntExactValue() {
        int[] expected = {-1244746321, 1060493871, -1826063944, 1976922248, -230127712,
                68408698, 169247282, -735843605, 2089114528, 1533708900};
        int[] actual = generateIntArray(expected.length, (IntSupplier) generator::nextInt);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLong16ExactValue() {
        long[] expected = {15L, 8L, 10L, 11L, 4L, 14L, 15L, 6L, 4L, 2L};
        long[] actual = generateLongArray(expected.length, () -> generator.nextLong(16));

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextLongExactValue() {
        long[] expected = {-5346144739450824145L, -7842884917907853176L, -988390996874898054L, 726911544686013163L,
                8972678576892185188L, 8222391730744523982L, -7363680848376404625L, -8294095623243520458L,
                -6307709242837825884L, -470456323649602622L};
        long[] actual = generateLongArray(expected.length, (LongSupplier) generator::nextLong);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.7101849056320706, 0.574836350385667, 0.9464192094792072, 0.03940595431138649,
                0.486409878091431, 0.4457367367074283, 0.6008140654988428, 0.5503761695842169,
                0.6580583901495687, 0.9744965039734514};
        double[] actual = generateDoubleArray(expected.length, generator::nextDouble);

        assertThat(actual, equalTo(expected));

    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.7101848F, 0.24691546F, 0.57483625F, 0.46028805F, 0.9464191F,
                0.015927553F, 0.039405942F, 0.828673F, 0.48640978F, 0.3570944F};
        float[] actual = generateFloatArray(expected.length, generator::nextFloat);

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextBooleanExactValue() {
        boolean[] expected = {true, false, true, false, true, false, false, true, false, false,
                false, false, true, false, true, true, true, false, true, false};
        boolean[] actual = generateBooleanArray(expected.length, generator::nextBoolean);

        assertThat(actual, equalTo(expected));
    }
}
