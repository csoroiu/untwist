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

import java.io.Serializable;
import java.util.Arrays;


/**
 * This class implements a powerful pseudo-random number generator
 * developed by Makoto Matsumoto and Takuji Nishimura during
 * 1996-1997.
 * <p>
 * This generator features an extremely long period
 * (2<sup>19937</sup>-1) and 623-dimensional equidistribution up to 32
 * bits accuracy. The home page for this generator is located at <a
 * href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html">
 * http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html</a>.
 * <p>
 * This generator is described in a paper by Makoto Matsumoto and
 * Takuji Nishimura in 1998: <a
 * href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/ARTICLES/mt.pdf">Mersenne
 * Twister: A 623-Dimensionally Equidistributed Uniform Pseudo-Random
 * Number Generator</a>, ACM Transactions on Modeling and Computer
 * Simulation, Vol. 8, No. 1, January 1998, pp 3--30
 * <p>
 * This class is mainly a Java port of the 2002-01-26 version of
 * the generator written in C by Makoto Matsumoto and Takuji
 * Nishimura. Here is their original copyright:
 * <table border="0" width="80%" cellpadding="10" align="center" bgcolor="#E0E0E0" summary="original copyright">
 * <tr><td>Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura,
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * <ol>
 * <li>Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.</li>
 * <li>The names of its contributors may not be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.</li>
 * </ol>
 * <p>
 * <strong>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.</strong></td></tr>
 * </table>
 */
public class ReversibleMersenneTwister extends ReverseBitsStreamGenerator implements Serializable {

    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Size of the bytes pool.
     */
    private static final int N = 624;

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

    // used to store the first and last element of the initial state
    // because, either first or last element can't be restored
    private final int[] mt_initial = new int[2];
    private long twists;

    /**
     * Creates a new random number generator.
     * <p>The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.</p>
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

    private void initGenRand(int seed) {
        // we use a long masked by 0xFFFFFFFFL as a poor man unsigned int
        long longMT = seed;
        // NB: unlike original C code, we are working with java longs, the cast below makes masking unnecessary
        mt[0] = (int) longMT;
        for (mti = 1; mti < N; ++mti) {
            // See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier.
            // initializer from the 2002-01-09 C version by Makoto Matsumoto
            longMT = (1812433253L * (longMT ^ (longMT >>> 30)) + mti) & 0xFFFFFFFFL;
            mt[mti] = (int) longMT;
        }

        clear(); // Clear normal deviate cache

        // Saving initial state separately
        mt_initial[0] = mt[0];
        mt_initial[1] = mt[N - 1];
        twists = 0;
    }

    /**
     * Reinitialize the generator as if just built with the given int seed.
     * <p>The state of the generator is exactly the same as a new
     * generator built with the same seed.</p>
     *
     * @param seed the initial seed (32 bits integer)
     */
    @Override
    public void setSeed(int seed) {
        initGenRand(seed);
    }

    /**
     * Reinitialize the generator as if just built with the given int array seed.
     * <p>The state of the generator is exactly the same as a new
     * generator built with the same seed.</p>
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be the current system time plus the
     *             system identity hash code of this instance
     */
    @Override
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

        clear(); // Clear normal deviate cache

        // Saving initial state separately
        mt_initial[0] = mt[0];
        mt_initial[1] = mt[N - 1];
        twists = 0;
    }

    /**
     * Reinitialize the generator as if just built with the given long seed.
     * <p>The state of the generator is exactly the same as a new
     * generator built with the same seed.</p>
     *
     * @param seed the initial seed (64 bits integer)
     */
    @Override
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (seed & 0xFFFFFFFFL)});
    }

    /**
     * Generate next pseudorandom number.
     * <p>This method is the core generation algorithm. It is used by all the
     * public generation methods for the various primitive types {@link
     * #nextBoolean()}, {@link #nextBytes(byte[])}, {@link #nextDouble()},
     * {@link #nextFloat()}, {@link #nextGaussian()}, {@link #nextInt()},
     * {@link #next(int)} and {@link #nextLong()}.</p>
     *
     * @param bits number of random bits to produce
     * @return random bits generated
     */
    @Override
    protected int next(int bits) {
        if (mti >= N) {
            twist();
            mti = 0;
        }
        return temper(mt[mti++]) >>> (32 - bits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int prev(int bits) {
        int result = temper(mt[--mti]) >>> (32 - bits);
        if (mti == 0) {
            untwist();
            mti = N;
        }
        return result;
    }

    private static int temper(int y) {
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9D2C5680;
        y ^= (y << 15) & 0xEFC60000;
        y ^= (y >>> 18);
        return y;
    }

    void twist() {
        for (int i = 0; i < N; i++) {
            int x = (mt[i] & 0x80000000) | (mt[(i + 1) % N] & 0x7FFFFFFF);
            mt[i] = mt[(i + M) % N] ^ (x >>> 1) ^ MAG01[x & 0x1];
        }
        if (++twists == 0) {
            mt[0] = mt_initial[0];
            mt[N - 1] = mt_initial[1];
        }
    }

    // https://jazzy.id.au/2010/09/22/cracking_random_number_generators_part_3.html
    // https://jazzy.id.au/2010/09/25/cracking_random_number_generators_part_4.html
    // https://adriftwith.me/coding/2010/08/13/reversing-the-mersenne-twister-rng-temper-function/
    void untwist() {
        for (int i = 623; i >= 0; i--) {
            int result;
            // first we calculate the first bit
            int tmp = mt[i];
            tmp ^= mt[(i + 397) % 624];
            // if the first bit is odd, unapply magic
            if ((tmp & 0x80000000) == 0x80000000) {
                tmp ^= 0x9908B0DF;
            }
            // the second bit of tmp is the first bit of the result
            result = (tmp << 1) & 0x80000000;

            // work out the remaining 31 bits
            tmp = mt[(i - 1 + 624) % 624];
            tmp ^= mt[(i + 396) % 624];
            if ((tmp & 0x80000000) == 0x80000000) {
                tmp ^= 0x9908B0DF;
                // since it was odd, the last bit must have been 1
                result |= 1;
            }
            // extract the final 30 bits
            result |= (tmp << 1) & 0x7FFFFFFF;
            mt[i] = result;
        }
        if (--twists == 0) {
            mt[0] = mt_initial[0];
            mt[N - 1] = mt_initial[1];
        }
    }

    int[] getState() {
        return Arrays.copyOf(mt, mt.length);
    }

    @SuppressWarnings("unused")
    private static int untemper(int y) {
        // https://www.randombit.net/bitbashing/2009/07/21/inverting_mt19937_tempering.html
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
}
