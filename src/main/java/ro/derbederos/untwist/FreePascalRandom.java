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
public class FreePascalRandom extends ReversibleMersenneTwister {
    // http://svn.freepascal.org/svn/fpc/trunk/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

    private static final long serialVersionUID = 1L;

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
     * of theMersenne Twister.
     */
    @Override
    public double nextDouble() {
        return toUnsignedLong(next(32)) * 0x1.0p-32d;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses only 32 bits to generate a double precision float number.
     */
    @Override
    public double prevDouble() {
        return toUnsignedLong(prev(32)) * 0x1.0p-32d;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply calls {@link #nextDouble()} and does a cast.
     * <p>
     * This method is the same as {@code genrand_real2} method in the
     * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original implementation</a>
     * of theMersenne Twister.
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
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L668">System.inc#random(longint)</a>
     */
    @Override
    public int nextInt(int n) throws IllegalArgumentException {
        if (n < 0) {
            n++;
        }
        long nextInt = toUnsignedLong(next(32));
        return (int) ((nextInt * n) >>> 32);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L668">System.inc#random(longint)</a>
     */
    @Override
    public int prevInt(int n) throws IllegalArgumentException {
        if (n < 0) {
            n++;
        }
        long prevInt = toUnsignedLong(prev(32));
        return (int) ((prevInt * n) >>> 32);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextLong() {
        long low = toUnsignedLong(next(32));
        long high = toUnsignedLong(next(32)) << 32;
        return high | low;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long prevLong() {
        long high = toUnsignedLong(prev(32)) << 32;
        long low = toUnsignedLong(prev(32));
        return low | high;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L676">System.inc#random(int64)</a>
     */
    @Override
    public long nextLong(long n) throws IllegalArgumentException {
        long low = toUnsignedLong(next(32));
        long high = toUnsignedLong(next(32)) & 0x7FFFFFFFL; // drop highest one bit
        long value = low | (high << 32);
        if (n != 0) {
            return value % n;
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
    public long prevLong(long n) throws IllegalArgumentException {
        long high = toUnsignedLong(prev(32)) & 0x7FFFFFFFL; // drop highest one bit
        long low = toUnsignedLong(prev(32));
        long value = low | (high << 32);
        if (n != 0) {
            return value % n;
        } else {
            return 0;
        }
    }
}
