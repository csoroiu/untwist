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

import static java.lang.Integer.toUnsignedLong;
import static java.lang.Math.abs;

/**
 * Java implementation of the random number generator used by FreePascal.
 * FreePascal uses Mersenne Twister, but it has to be initialized with a
 * 32bit seed in order to get the same values.
 * <p>
 * Also there are some slight differences on how different things are
 * generated of the values provided from the Mersenne Twister. One of them
 * is the {@link #nextDouble()} method which is using only 32 random bits
 * to get a double precision float.
 * <p>
 * The source code used is <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L531">System.inc</a>
 */
public class FreePascalRandom extends ReversibleMersenneTwister implements ReverseNormalizedGaussianSampler {
    private static final long serialVersionUID = 1L;

    private static final double DOUBLE_UNIT = 0x1.0p-32d; // 1.0 / (1L << 32)

    /**
     * Creates a new random number generator.
     * <p>
     * The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.
     */
    public FreePascalRandom() {
        super();
    }

    /**
     * Creates a new random number generator using a single int seed.
     *
     * @param seed the initial seed (32 bits integer).
     * @see #setSeed(int)
     */
    public FreePascalRandom(int seed) {
        this(new int[]{seed});
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be related to the current time.
     * @see #setSeed(int[])
     */
    public FreePascalRandom(int[] seed) {
        super(seed);
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed the initial seed (64 bits integer).
     * @see #setSeed(long)
     */
    public FreePascalRandom(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the high 32 bits are 0, then it calls {@link #setSeed(int)}.
     * Otherwise it calls {@link #setSeed(int[])} with the low and high 32 bit numbers.
     */
    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed((int) seed);
        } else {
            setSeed(new int[]{(int) (seed & 0xFFFFFFFFL), high});
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the array contains only one value, then it calls {@link #setSeed(int)}.
     */
    @Override
    public void setSeed(int[] seed) {
        if (seed.length == 1) {
            setSeed(seed[0]);
        } else {
            super.setSeed(seed);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses only 32 bits to generate a double precision float number.
     * <p>
     * This method is the same as {@code genrand_real2} method in the
     * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original implementation</a>
     * of the Mersenne Twister.
     */
    @Override
    public double nextDouble() {
        return toUnsignedLong(next(32)) * DOUBLE_UNIT;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses only 32 bits to generate a double precision float number.
     */
    @Override
    public double prevDouble() {
        return toUnsignedLong(prev(32)) * DOUBLE_UNIT;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply calls {@link #nextDouble()} and does a cast.
     * <p>
     * This method is the same as {@code genrand_real2} method in the
     * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original implementation</a>
     * of the Mersenne Twister.
     */
    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply calls {@link #prevDouble()} and does a cast.
     */
    @Override
    public float prevFloat() {
        return (float) prevDouble();
    }

    /**
     * {@inheritDoc}
     *
     * @implNote <b>This method is not exposed in FreePascal.</b>
     * Maybe <a href="https://bugs.freepascal.org/view.php?id=31693">https://bugs.freepascal.org/view.php?id=31693</a>
     * or <a href="https://bugs.freepascal.org/view.php?id=31633">https://bugs.freepascal.org/view.php?id=31633</a>
     * will expose it.
     */
    @Override
    public int nextInt() {
        return next(32);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int prevInt() {
        return prev(32);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L668">System.inc#Random(longint)</a>
     */
    @Override
    public int nextInt(int bound) throws IllegalArgumentException {
        if (bound < 0) {
            bound++;
        }
        long nextInt = toUnsignedLong(next(32));
        return (int) ((nextInt * bound) >>> 32);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L668">System.inc#Random(longint)</a>
     */
    @Override
    public int prevInt(int bound) throws IllegalArgumentException {
        if (bound < 0) {
            bound++;
        }
        long prevInt = toUnsignedLong(prev(32));
        return (int) ((prevInt * bound) >>> 32);
    }

    /**
     * Similar to FreePascal's {@code Math.RandomRange} and returns a random integer from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     * <p>
     * Source code:
     * <a href=https://github.com/graemeg/freepascal/blob/5186987/rtl/objpas/math.pp#L1325>math.pp#RandomRange(Integer, Integer)</a>
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    @Override
    public int nextInt(int from, int to) {
        return (int) nextLong(from, to);
    }

    /**
     * The reverse of {@link #nextInt(int, int)}.
     * <p>
     * Returns a random integer from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     * <p>
     * Source code:
     * <a href=https://github.com/graemeg/freepascal/blob/5186987/rtl/objpas/math.pp#L1325>math.pp#RandomRange(Integer, Integer)</a>
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    @Override
    public int prevInt(int from, int to) {
        return (int) prevLong(from, to);
    }

    /**
     * Similar to FreePascal's {@code Math.RandomRange} and returns a random long from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     * <p>
     * Source code:
     * <a href=https://github.com/graemeg/freepascal/blob/5186987/rtl/objpas/math.pp#L1331>math.pp#RandomRange(Int64, Int64)</a>
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    public long nextLong(long from, long to) {
        return nextLong(abs(to - from)) + Math.min(from, to);
    }

    /**
     * The reverse of {@link #nextLong(long, long)}.
     * <p>
     * Returns a random long from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     * <p>
     * Source code:
     * <a href=https://github.com/graemeg/freepascal/blob/5186987/rtl/objpas/math.pp#L1325>math.pp#RandomRange(Integer, Integer)</a>
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    public long prevLong(long from, long to) {
        return prevLong(abs(to - from)) + Math.min(from, to);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote <b>This method is not part of FreePascal implementation.</b>
     */
    @Override
    public long nextLong() {
        long low = toUnsignedLong(next(32));
        long high = toUnsignedLong(next(32)) << 32;
        return low | high;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long prevLong() {
        long high = toUnsignedLong(prev(32)) << 32;
        long low = toUnsignedLong(prev(32));
        return high | low;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L676">System.inc#random(int64)</a>
     */
    @Override
    public long nextLong(long bound) throws IllegalArgumentException {
        long low = toUnsignedLong(next(32));
        long high = toUnsignedLong(next(32)) & 0x7FFFFFFFL; // drop highest one bit
        long value = low | (high << 32);
        if (bound != 0) {
            return value % bound;
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L676">System.inc#random(int64)</a>
     */
    @Override
    public long prevLong(long bound) throws IllegalArgumentException {
        long high = toUnsignedLong(prev(32)) & 0x7FFFFFFFL; // drop highest one bit
        long low = toUnsignedLong(prev(32));
        long value = low | (high << 32);
        if (bound != 0) {
            return value % bound;
        } else {
            return 0;
        }
    }

    /**
     * Similar to FreePascal's {@code Math.RandG(0, 1)} and returns the next pseudorandom, Gaussian ("normally") distributed
     * {@code double} value with mean {@code 0.0} and standard deviation {@code 1.0} from this random number generator's sequence.
     * <p>
     * FreePascal and Delphi use <a href="https://en.wikipedia.org/wiki/Marsaglia_polar_method#Implementation">Marsaglia polar method</a>.
     * <p>
     * Source code:
     * <a href=https://github.com/graemeg/freepascal/blob/5186987/rtl/objpas/math.pp#L1314>math.pp#RandG(float, float)</a>
     *
     * @return the next pseudorandom, Gaussian ("normally") distributed {@code double} value with mean {@code 0.0}
     * and standard deviation {@code 1.0} from this random number generator's sequence.
     */
    @Override
    public double nextGaussian() {
        // Marsaglia-Bray algorithm, which generates 2 values at a time, one is dropped (multiplier * u2)
        double u1, u2, s2;
        do {
            u1 = 2 * nextDouble() - 1; // between -1 and 1
            u2 = 2 * nextDouble() - 1; // between -1 and 1
            s2 = u1 * u1 + u2 * u2;
        } while (s2 >= 1);
        double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s2) / s2);
        return multiplier * u1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoNextGaussian() {
        double v1, v2, s;
        do {
            v1 = 2 * prevDouble() - 1; // between -1 and 1
            v2 = 2 * prevDouble() - 1; // between -1 and 1
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
    }
}
