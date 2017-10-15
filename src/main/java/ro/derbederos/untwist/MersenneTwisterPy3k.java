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
 * A Mersenne Twister subclass which generates the same numbers as the Python 3 implementation.
 * Source code uses as reference is part of the files:
 * <ul>
 * <li><a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.</li>
 * <li><a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.</li>
 * <li><a href="https://github.com/python/cpython/blob/master/Lib/test/test_random.py">test_random.py</a>.</li>
 * </ul>
 * Test
 */
public class MersenneTwisterPy3k extends ReversibleMersenneTwister implements ReverseNormalizedGaussianSampler {

    private static final long serialVersionUID = 1L;

    private static final double DOUBLE_UNIT = 0x1.0p-53d; // 1.0 / (1L << 53)

    /**
     * Creates a new random number generator.
     * <p>
     * The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.
     */
    public MersenneTwisterPy3k() {
        super();
    }

    /**
     * Creates a new random number generator using a single int seed.
     *
     * @param seed the initial seed (32 bits integer).
     * @see #setSeed(int)
     */
    public MersenneTwisterPy3k(int seed) {
        this(new int[]{seed});
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be related to the current time.
     * @see #setSeed(int[])
     */
    public MersenneTwisterPy3k(int[] seed) {
        super(seed);
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed the initial seed (64 bits integer).
     * @see #setSeed(long)
     */
    public MersenneTwisterPy3k(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It calls {@link #setSeed(int[])}.
     */
    @Override
    public void setSeed(int seed) {
        setSeed(new int[]{seed});
        clear();
    }

    /**
     * {@inheritDoc}
     * <p>
     * It calls {@link #setSeed(int[])}.
     */
    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed(new int[]{(int) seed});
        } else {
            setSeed(new int[]{(int) (seed & 0xFFFFFFFFL), high});
        }
        clear();
    }

    @Override
    public void setSeed(int[] seed) {
        super.setSeed(seed);
        clear();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code random_random} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     * <p>
     * This method is the same as {@code genrand_res53} method in the
     * <a href="http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/MT2002/CODES/mt19937ar.c">original implementation</a>
     * of the Mersenne Twister.
     */
    @Override
    public double nextDouble() {
        return (((long) (next(27)) << 26) + next(26)) * DOUBLE_UNIT;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code random_random} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     */
    @Override
    public double prevDouble() {
        return (prev(26) + ((long) (prev(27)) << 26)) * DOUBLE_UNIT;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply calls {@link #nextDouble()} and does a cast.
     * <p>
     * Source code: {@code random_random} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     * <p>
     * This method is the same as {@code genrand_res53} method in the
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
     * <p>
     * Source code: {@code _randbelow} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.
     */
    @Override
    public int nextInt(int bound) throws IllegalArgumentException {
        if (bound > 0) {
            final int bit_length = Integer.SIZE - Integer.numberOfLeadingZeros(bound);
            int bits;
            do {
                bits = next(bit_length);
            } while (bits >= bound);
            return bits;
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code _randbelow} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.
     */
    @Override
    public int prevInt(int bound) throws IllegalArgumentException {
        if (bound > 0) {
            final int bit_length = Integer.SIZE - Integer.numberOfLeadingZeros(bound);
            int bits;
            do {
                bits = prev(bit_length);
            } while (bits >= bound);
            return bits;
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Algorithm for {@link #nextLong()} is similar to the one for {@link #nextBytes(byte[])}
     * when called for 8 bytes.
     * <p>
     * Source code: {@code random_getrandbits} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     */
    @Override
    public long nextLong() {
        long low = toUnsignedLong(next(32));
        long high = toUnsignedLong(next(32)) << 32;
        return low | high;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Algorithm for {@link #prevLong()} is similar to the one for {@link #prevBytes(byte[])}
     * when called for 8 bytes.
     * <p>
     * Source code: {@code random_getrandbits} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
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
     * Source code: {@code random_getrandbits} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     */
    @Override
    protected void nextBytesFill(byte[] bytes, int start, int len) {
        int i = start;
        final int endIndex = start + len;
        final int iEnd = endIndex - 4;
        while (i < iEnd) {
            final int random = next(32);
            bytes[i] = (byte) (random & 0xFF);
            bytes[i + 1] = (byte) ((random >>> 8) & 0xFF);
            bytes[i + 2] = (byte) ((random >>> 16) & 0xFF);
            bytes[i + 3] = (byte) ((random >>> 24) & 0xFF);
            i += 4;
        }
        int random = next(32);
        final int k = (endIndex - i) * 8;
        random >>>= 32 - k;
        while (i < endIndex) {
            bytes[i++] = (byte) (random & 0xFF);
            random >>= 8;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code random_getrandbits} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Modules/_randommodule.c">_randommodule.c</a>.
     */
    @Override
    protected void prevBytesFill(byte[] bytes, int start, int len) {
        int i = start;
        int iEnd = len - (len & 0x7ffffffc);
        if (iEnd != 0) {
            int random = prev(32);
            int indexLoopLimit = start + iEnd;
            while (i < indexLoopLimit) {
                bytes[i++] = (byte) ((random >> 24) & 0xFF);
                random <<= 8;
            }
        }
        int endIndex = start + len;
        while (i < endIndex) {
            final int random = prev(32);
            bytes[i] = (byte) ((random >>> 24) & 0xFF);
            bytes[i + 1] = (byte) ((random >>> 16) & 0xFF);
            bytes[i + 2] = (byte) ((random >>> 8) & 0xFF);
            bytes[i + 3] = (byte) (random & 0xFF);
            i += 4;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code _randbelow} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.
     */
    @Override
    public long nextLong(long bound) throws IllegalArgumentException {
        if (bound > 0) {
            final int bit_length = Long.SIZE - Long.numberOfLeadingZeros(bound);
            long bits;
            do {
                bits = toUnsignedLong(next(Math.min(32, bit_length)));
                if (bit_length > 32) {
                    bits = bits | toUnsignedLong(next(bit_length - 32)) << 32;
                }
            } while (bits >= bound);
            return bits;
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code: {@code _randbelow} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.
     */
    @Override
    public long prevLong(long bound) throws IllegalArgumentException {
        if (bound > 0) {
            final int bit_length = Long.SIZE - Long.numberOfLeadingZeros(bound);
            long bits;
            do {
                bits = 0;
                if (bit_length > 32) {
                    bits = toUnsignedLong(prev(bit_length - 32)) << 32;
                }
                bits |= toUnsignedLong(prev(Math.min(32, bit_length)));
            } while (bits >= bound);
            return bits;
        }
        throw new IllegalArgumentException("bound must be strictly positive");
    }

    // the state of the generator
    private boolean shouldReverseGaussian;
    private double nextGaussian;

    /**
     * Clears the cache used by the default implementation of
     * {@link #nextGaussian}.
     */
    @Override
    public void clear() {
        nextGaussian = Double.NaN;
        shouldReverseGaussian = false;
    }

    /**
     * It uses a variation of <a href="https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform#Implementation">Box-Müller transform</a>.
     * <p>
     * Source code: {@code gauss} method in the file
     * <a href="https://github.com/python/cpython/blob/master/Lib/random.py">random.py</a>.
     */
    @Override
    public double nextGaussian() {
        shouldReverseGaussian = !shouldReverseGaussian;

        final double random;
        if (Double.isNaN(nextGaussian)) {
            // generate a new pair of gaussian numbers
            final double x = nextDouble();
            final double y = nextDouble();
            final double alpha = 2 * Math.PI * x;
            final double r = StrictMath.sqrt(-2 * StrictMath.log(1 - y));
            random = r * StrictMath.cos(alpha);
            nextGaussian = r * StrictMath.sin(alpha);
        } else {
            // use the second element of the pair already generated
            random = nextGaussian;
            nextGaussian = Double.NaN;
        }

        return random;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoNextGaussian() {
        if (shouldReverseGaussian) {
            prevDouble();
            prevDouble();
            clear();
        } else {
            shouldReverseGaussian = true;
        }
    }
}
