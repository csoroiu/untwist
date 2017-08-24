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
 * Java implementation of the random number generator from Turbo Pascal 7/Delphi.
 * It is a <a href="https://en.wikipedia.org/wiki/Linear_congruential_generator">linear congruential generator</a>
 * and the next seed is computed using the formula:
 * <pre>{@code seed = (seed * 0x08088405 + 1) & 0xFFFFFFFF}.</pre>
 * The previous seed is computed using the formula:
 * <pre>{@code seed = ((seed - 1) * 0xD94FA8CD) & 0xFFFFFFFF}.</pre>
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
    private static final long MASK = (1L << 32) - 1;

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
     * Converts the {@code int[]} seed to an {@code int} and calls {@link #setSeed(int)}.
     *
     * @param seed an array used to calculate a starting value for the pseudo-random number sequence.
     * @see RandomUtils#convertToInt(int...)
     */
    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToInt(seed));
    }

    /**
     * Converts the {@code long} seed to an {@code int} and calls {@link #setSeed(int)}.
     *
     * @param seed a number used to calculate a starting value for the pseudo-random number sequence.
     * @see RandomUtils#convertToInt(long)
     */
    @Override
    public void setSeed(long seed) {
        setSeed(RandomUtils.convertToInt(seed));
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
     */
    @Override
    public int nextInt(int n) {
        if (n > 0) {
            long nextInt = next(32) & 0xFFFFFFFFL;
            return (int) ((nextInt * n) >>> 32);
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int prevInt(int n) {
        if (n > 0) {
            long prevInt = prev(32) & 0xFFFFFFFFL;
            return (int) ((prevInt * n) >>> 32);
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt">_randExt</a>.
     */
    @Override
    public double nextDouble() {
        long nextInt = next(32) & 0xFFFFFFFFL;
        if (coprocessorEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return (int) nextInt / (double) (1L << 32) + 0.5;
        } else {
            return nextInt / (double) (1L << 32);
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
        long prevInt = prev(32) & 0xFFFFFFFFL;
        if (coprocessorEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return (int) prevInt / (double) (1L << 32) + 0.5;
        } else {
            return prevInt / (double) (1L << 32);
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
}
