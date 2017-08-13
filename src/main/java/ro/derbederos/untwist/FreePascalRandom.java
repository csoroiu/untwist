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
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
    // http://svn.freepascal.org/svn/fpc/trunk/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new random number generator.
     * <p>The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.</p>
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
     */
    @Override
    public void setSeed(int seed) {
        super.setSeed(seed);
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
            setSeed(new int[]{high, (int) (seed & 0xFFFFFFFFL)});
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
            super.setSeed(reverseArray(seed));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses only 32 bits to generate a double precision float number.
     */
    @Override
    public double nextDouble() {
        return ((long) next(32) & 0xFFFFFFFFL) * 0x1.0p-32d;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses only 32 bits to generate a double precision float number.
     */
    @Override
    public double prevDouble() {
        return ((long) prev(32) & 0xFFFFFFFFL) * 0x1.0p-32d;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply calls {@link #nextDouble()} and does a cast.
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
        long urand = ((long) next(32)) & 0xFFFFFFFFL;
        return (int) ((urand * n) >>> 32);
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
        long urand = ((long) prev(32)) & 0xFFFFFFFFL;
        return (int) ((urand * n) >>> 32);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long nextLong() {
        final long low = ((long) next(32)) & 0xFFFFFFFFL;
        final long high = ((long) next(32)) << 32;
        return high | low;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long prevLong() {
        final long high = ((long) prev(32)) << 32;
        final long low = ((long) prev(32)) & 0xFFFFFFFFL;
        return high | low;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L676">System.inc#random(int64)</a>
     */
    public long nextLong(long n) throws IllegalArgumentException {
        long low = ((long) next(32)) & 0xFFFFFFFFL;
        long high = ((long) next(32)) & 0x7FFFFFFFL; // drop highest one bit
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
        long high = ((long) prev(32)) & 0x7FFFFFFFL; // drop highest one bit
        long low = ((long) prev(32)) & 0xFFFFFFFFL;
        long value = low | (high << 32);
        if (n != 0) {
            return value % n;
        } else {
            return 0;
        }
    }

    private static int[] reverseArray(int[] seed) {
        int[] seedReversed = new int[seed.length];
        int j = seed.length;
        for (int i = 0; i < seedReversed.length; i++) {
            seedReversed[i] = seed[--j];
        }
        return seedReversed;
    }
}
