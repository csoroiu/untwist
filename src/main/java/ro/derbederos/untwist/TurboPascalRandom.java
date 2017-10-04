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
 * Java implementation of the random number generator from Turbo Pascal 7/Delphi.
 * It is a <a href="https://en.wikipedia.org/wiki/Linear_congruential_generator">linear congruential generator</a>
 * and the next seed is computed using the formula:
 * <pre>{@code seed = (seed * 0x08088405 + 1) & 0xFFFFFFFFL}.</pre>
 * The previous seed is computed using the formula:
 * <pre>{@code seed = ((seed - 1) * 0xD94FA8CD) & 0xFFFFFFFFL}.</pre>
 * In Turbo Pascal 7 there was a switch that was enabling the use of the coprocessor.
 * There is a difference of {@code 0.5} when generating a random floating number when
 * coprocessor was disabled {@code {$N-}} or enabled {@code {$N+}}.
 * Delphi maintains compatibility with the {@code {$N-}} version.
 * When enabling coprocessor {@code {$N+}}, in Turbo Pascal 7, the values returned by random
 * function are offset-ed by {@code 0.5} from the usual ones. If value is {@code >=0.5}
 * then it is decreased by {@code 0.5} else value is increased by {@code 0.5}.
 * This seems to be a <a href="https://groups.google.com/d/msg/borland.public.delphi.language.objectpascal/_3BBDQ0X5H0/WlGuqooixE0J">
 * design fault</a>, as 32 bit numbers are emulated in Turbo Pascal 7.
 */
public class TurboPascalRandom extends ReverseBitsStreamGenerator {
    private static final long serialVersionUID = 1L;

    private static final long MULTIPLIER = 0x08088405L;
    private static final long INVERSE_MULTIPLIER = 0xD94FA8CDL;
    private static final long ADDEND = 0x1L;
    private static final long MASK = 0xFFFFFFFFL;         // (1L << 32) - 1
    private static final double DOUBLE_UNIT = 0x1.0p-32d; // 1.0 / (1L << 32)

    private final boolean coprocessorEnabled;

    private long seed;

    /**
     * Creates a new random number generator using a single int seed.
     * Delphi compatible.
     *
     * @param seed the initial seed (32 bits integer).
     * @see #setSeed(int)
     */
    public TurboPascalRandom(int seed) {
        this(seed, false);
    }

    /**
     * Creates a new random number generator using an int array seed.
     * Delphi compatible.
     *
     * @param seed the initial seed (32 bits integers array).
     * @see #setSeed(int[])
     */
    public TurboPascalRandom(int[] seed) {
        this(seed, false);
    }

    /**
     * Creates a new random number generator using a single long seed.
     * Delphi compatible.
     *
     * @param seed the initial seed (64 bits integer).
     * @see #setSeed(long)
     */
    public TurboPascalRandom(long seed) {
        this(seed, false);
    }

    /**
     * Creates a new random number generator using a single int seed.
     *
     * @param seed               the initial seed (32 bits integer).
     * @param coprocessorEnabled flag that enables the offsetting by {@code 0.5} of the generated floating number.
     * @see #setSeed(int)
     */
    protected TurboPascalRandom(int seed, boolean coprocessorEnabled) {
        setSeed(seed);
        this.coprocessorEnabled = coprocessorEnabled;
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed               the initial seed (32 bits integers array).
     * @param coprocessorEnabled flag that enables the offsetting by {@code 0.5} of the generated floating number.
     * @see #setSeed(int[])
     */
    protected TurboPascalRandom(int[] seed, boolean coprocessorEnabled) {
        setSeed(seed);
        this.coprocessorEnabled = coprocessorEnabled;
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed               the initial seed (64 bits integer).
     * @param coprocessorEnabled flag that enables the offsetting by {@code 0.5} of the generated floating number.
     * @see #setSeed(long)
     */
    protected TurboPascalRandom(long seed, boolean coprocessorEnabled) {
        setSeed(seed);
        this.coprocessorEnabled = coprocessorEnabled;
    }

    /**
     * Initializes this instance of the {@link TurboPascalRandom} class, using the specified seed value.
     *
     * @param seed a number used to calculate a starting value for the pseudo-random number sequence.
     */
    @Override
    public void setSeed(int seed) {
        this.seed = seed;
        clear();
    }

    /**
     * Returns the seed used by this generator.
     *
     * @return the seed (which can be used with {@link #setSeed(long)}.
     */
    public int getSeed() {
        return (int) seed;
    }

    /**
     * Converts the {@code int[]} seed to an {@code int} and calls {@link #setSeed(int)}.
     *
     * @param seed an array used to calculate a starting value for the pseudo-random number sequence.
     * @see SeedUtils#convertToInt(int...)
     */
    @Override
    public void setSeed(int[] seed) {
        setSeed(SeedUtils.convertToInt(seed));
    }

    /**
     * Converts the {@code long} seed to an {@code int} and calls {@link #setSeed(int)}.
     *
     * @param seed a number used to calculate a starting value for the pseudo-random number sequence.
     * @see SeedUtils#convertToInt(long)
     */
    @Override
    public void setSeed(long seed) {
        setSeed(SeedUtils.convertToInt(seed));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int next(int bits) {
        seed = (seed * MULTIPLIER + ADDEND) & MASK;
        return (int) (seed >>> (32 - bits));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int prev(int bits) {
        int result = (int) (seed >>> (32 - bits));
        seed = ((seed - ADDEND) * INVERSE_MULTIPLIER) & MASK;
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>This method is not available in Turbo Pascal directly.</b>
     */
    @Override
    public int nextInt() {
        return super.nextInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int prevInt() {
        return super.prevInt();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Preserves compatibility with Pascal where negative values are not allowed for parameter {@code n}.
     * Delphi allows negative values and treats {@code n} as unsigned in the computations, but the results
     * is still a signed integer.
     */
    @Override
    public int nextInt(int bound) {
        if (bound > 0) {
            return nextIntUnsigned(bound);
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    private int nextIntUnsigned(int bound) {
        long nextInt = toUnsignedLong(next(32));
        return (int) ((nextInt * toUnsignedLong(bound)) >>> 32);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int prevInt(int bound) {
        if (bound > 0) {
            return prevIntUnsigned(bound);
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    private int prevIntUnsigned(int bound) {
        long prevInt = toUnsignedLong(prev(32));
        return (int) ((prevInt * toUnsignedLong(bound)) >>> 32);
    }

    /**
     * Similar to Delphi's {@code Math.RandomRange} and returns a random integer from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    @Override
    public int nextInt(int from, int to) {
        if (from > to) {
            return nextIntUnsigned(from - to) + to;
        } else {
            return nextIntUnsigned(to - from) + from;
        }
    }

    /**
     * The reverse of {@link #nextInt(int, int)}.
     * <p>
     * Returns a random integer from the range that extends between {@code from} and {@code to} (exclusive).
     * It can handle negative ranges (where {@code from} is greater than {@code to}).
     *
     * @param from the least value, unless greater than {@code to}.
     * @param to   the upper bound (exclusive), unless lower than {@code from}.
     * @return a random integer from a specified range.
     */
    @Override
    public int prevInt(int from, int to) {
        if (from > to) {
            return prevIntUnsigned(from - to) + to;
        } else {
            return prevIntUnsigned(to - from) + from;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt">_randExt</a>.
     */
    @Override
    public double nextDouble() {
        int nextInt = next(32);
        if (coprocessorEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return nextInt * DOUBLE_UNIT + 0x1p-1d; // 0x1p-1d = 0.5d
        } else {
            // delphi is performing unsigned division
            return toUnsignedLong(nextInt) * DOUBLE_UNIT;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt">_randExt</a>.
     */
    @Override
    public double prevDouble() {
        int prevInt = prev(32);
        if (coprocessorEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return prevInt * DOUBLE_UNIT + 0x1p-1d; // 0x1p-1d = 0.5d
        } else {
            // delphi is performing unsigned division
            return toUnsignedLong(prevInt) * DOUBLE_UNIT;
        }
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
     * Similar to FreePascal's {@code Math.RandG(0, 1)} and returns the next pseudorandom, Gaussian ("normally") distributed
     * {@code double} value with mean {@code 0.0} and standard deviation {@code 1.0} from this random number generator's sequence.
     * <p>
     * FreePascal and Delphi use <a href="https://en.wikipedia.org/wiki/Marsaglia_polar_method#Implementation">Marsaglia polar method</a>.
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
        clear();
    }
}
