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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class ReversibleMersenneTwister extends ReverseBitsStreamGenerator implements Serializable {

    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Size of the bytes pool.
     */
    private static final int N = 624;

    private static final int STATE_SIZE = N * Integer.BYTES + Integer.BYTES; //mt, mti

    /**
     * Period second parameter.
     */
    private static final int M = 397;

    /**
     * X * MATRIX_A for X = {0, 1}.
     */
    private static final int[] MAG01 = {0x0, 0x9908B0DF};

    /**
     * Bytes pool.
     */
    private final int[] mt = new int[N];

    /**
     * Current index in the bytes pool.
     */
    private int mti;

    /**
     * Creates a new random number generator.
     * <p>
     * The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.
     */
    public ReversibleMersenneTwister() {
        setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    /**
     * Creates a new random number generator using a single int seed.
     *
     * @param seed the initial seed (32 bits integer)
     */
    public ReversibleMersenneTwister(int seed) {
        setSeed(seed);
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be related to the current time
     */
    public ReversibleMersenneTwister(int[] seed) {
        setSeed(seed);
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed the initial seed (64 bits integer)
     */
    public ReversibleMersenneTwister(long seed) {
        setSeed(seed);
    }

    /**
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     * Method {@code init_genrand}.
     *
     * @param seed the initial seed
     */
    private void initGenRand(int seed) {
        // we use a long masked by 0xFFFFFFFFL as a poor man unsigned int
        long longMT = Integer.toUnsignedLong(seed);
        // NB: unlike original C code, we are working with java longs, the cast below makes masking unnecessary
        mt[0] = (int) longMT;
        for (mti = 1; mti < N; ++mti) {
            // See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier.
            // initializer from the 2002-01-09 C version by Makoto Matsumoto
            longMT = (1812433253L * (longMT ^ (longMT >>> 30)) + mti) & 0xFFFFFFFFL;
            mt[mti] = (int) longMT;
        }
    }

    /**
     * Reinitialize the generator as if just built with the given int seed.
     * <p>
     * The state of the generator is exactly the same as a new
     * generator built with the same seed.
     * <p>
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     * Method {@code init_genrand}.
     *
     * @param seed the initial seed (32 bits integer)
     */
    public void setSeed(int seed) {
        initGenRand(seed);
        fixState();
    }

    /**
     * Reinitialize the generator as if just built with the given int array seed.
     * <p>
     * The state of the generator is exactly the same as a new
     * generator built with the same seed.
     * <p>
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     * Method {@code init_by_array}.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be the current system time plus the
     *             system identity hash code of this instance
     */
    public void setSeed(int[] seed) {

        if (seed == null) {
            setSeed(System.currentTimeMillis() + System.identityHashCode(this));
            return;
        }

        initGenRand(19650218);
        int i = 1;
        int j = 0;

        for (int k = Math.max(N, seed.length); k != 0; k--) {
            long l0 = (mt[i] & 0x7FFFFFFFL) | ((mt[i] < 0) ? 0x80000000L : 0x0L);
            long l1 = (mt[i - 1] & 0x7FFFFFFFL) | ((mt[i - 1] < 0) ? 0x80000000L : 0x0L);
            long l = (l0 ^ ((l1 ^ (l1 >>> 30)) * 1664525L)) + seed[j] + j; // non linear
            mt[i] = (int) (l & 0xFFFFFFFFL);
            i++;
            j++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
            if (j >= seed.length) {
                j = 0;
            }
        }

        for (int k = N - 1; k != 0; k--) {
            long l0 = (mt[i] & 0x7FFFFFFFL) | ((mt[i] < 0) ? 0x80000000L : 0x0L);
            long l1 = (mt[i - 1] & 0x7FFFFFFFL) | ((mt[i - 1] < 0) ? 0x80000000L : 0x0L);
            long l = (l0 ^ ((l1 ^ (l1 >>> 30)) * 1566083941L)) - i; // non linear
            mt[i] = (int) (l & 0xFFFFFFFFL);
            i++;
            if (i >= N) {
                mt[0] = mt[N - 1];
                i = 1;
            }
        }

        mt[0] = 0x80000000; // MSB is 1; assuring non-zero initial array
        fixState();
    }

    /**
     * Reinitialize the generator as if just built with the given long seed.
     * <p>
     * The state of the generator is exactly the same as a new
     * generator built with the same seed.
     *
     * @param seed the initial seed (64 bits integer)
     */
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (seed & 0xFFFFFFFFL)});
    }

    private void fixState() {
        // fixing the state so going in reverse first
        // and next go forward should work just fine
        twist();
        untwist();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     */
    @Override
    public int next() {
        if (mti >= N) {
            twist();
            mti = 0;
        }
        return temper(mt[mti++]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int prev() {
        int result = temper(mt[--mti]);
        if (mti == 0) {
            untwist();
            mti = N;
        }
        return result;
    }

    /**
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     * The block of code which computes the next {@code mt} array from the original method {@code genrand_int32}.
     */
    void twist() {
        for (int i = 0; i < N; i++) {
            int x = (mt[i] & 0x80000000) | (mt[(i + 1) % N] & 0x7FFFFFFF);
            mt[i] = mt[(i + M) % N] ^ (x >>> 1) ^ MAG01[x & 0x1];
        }
    }

    /**
     * Part of the <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original code</a>.
     * The tempering part from the original method {@code genrand_int32}.
     *
     * @param y the value to temper.
     * @return The tempered value.
     */
    static int temper(int y) {
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9D2C5680;
        y ^= (y << 15) & 0xEFC60000;
        y ^= (y >>> 18);
        return y;
    }

    /**
     * The reverse of {@link #twist()} method.
     * <p>
     * Source code:
     * <a href="https://jazzy.id.au/2010/09/25/cracking_random_number_generators_part_4.html">Cracking Random Number Generators - Part 4</a>
     * <a href="https://jazzy.id.au/2010/09/22/cracking_random_number_generators_part_3.html">Cracking Random Number Generators - Part 3</a>
     */
    void untwist() {
        for (int i = 623; i >= 0; i--) {
            int result;
            // first we calculate the first bit
            int tmp = mt[i] ^ mt[(i + M) % N];
            // if the first bit is odd, unapply magic
            if ((tmp & 0x80000000) == 0x80000000) {
                tmp ^= 0x9908B0DF;
            }
            // the second bit of tmp is the first bit of the result
            result = (tmp << 1) & 0x80000000;

            // work out the remaining 31 bits
            tmp = mt[(i - 1 + N) % N] ^ mt[(i + M - 1) % N];
            if ((tmp & 0x80000000) == 0x80000000) {
                tmp ^= 0x9908B0DF;
                // since it was odd, the last bit must have been 1
                result |= 1;
            }
            // extract the final 30 bits
            result |= (tmp << 1) & 0x7FFFFFFF;
            mt[i] = result;
        }
    }

    /**
     * The reverse of {@link #temper(int)} method.
     * <p>
     * Source code/references:
     * <ul>
     * <li><a href="https://www.randombit.net/bitbashing/2009/07/21/inverting_mt19937_tempering.html">
     * Inverting Mersenne Twister’s final transform</a></li>
     * <li><a href="https://adriftwith.me/coding/2010/08/13/reversing-the-mersenne-twister-rng-temper-function/">
     * Reversing the Mersenne Twister RNG Temper Function</a></li>
     * </ul>
     *
     * @param y the value to untemper.
     * @return The untempered value.
     */
    static int untemper(int y) {
        y ^= (y >>> 18);
        y ^= (y << 15) & 0xEFC60000;
        y ^= (y << 7) & 0x1680;
        y ^= (y << 7) & 0xC4000;
        y ^= (y << 7) & 0xD200000;
        y ^= (y << 7) & 0x90000000;
        y ^= (y >>> 11) & 0xFFC00000;
        y ^= (y >>> 11) & 0x3FF800;
        y ^= (y >>> 11) & 0x7FF;
        return y;
    }

    byte[] getState() {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(STATE_SIZE);
        DataOutputStream output = new DataOutputStream(byteOutput);
        try {
            output.writeInt(mti);
            for (int value : mt) {
                output.writeInt(value);
            }
            output.close();
        } catch (IOException ignore) {
        }
        return byteOutput.toByteArray();
    }
}
