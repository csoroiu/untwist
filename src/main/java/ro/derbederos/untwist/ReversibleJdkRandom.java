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

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class extends the random class and ads methods for generating random values in reverse.
 */
public class ReversibleJdkRandom extends Random implements ReverseRandomGenerator {
    private static final long serialVersionUID = 1L;

    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long INVERSE_MULTIPLIER = 0xDFE05BCB1365L;
    private static final long ADDEND = 0xBL;
    private static final long MASK = (1L << 48) - 1;
    private static final double DOUBLE_UNIT = 0x1.0p-53d; // 1.0 / (1L << 53)

    // the state of the generator
    private final AtomicLong seed = new AtomicLong();
    private transient long tempSeed;
    private boolean shouldReverseGaussian;
    private boolean hasNextGaussian;

    public ReversibleJdkRandom() {
        super();
        setSeed(tempSeed);
    }

    public ReversibleJdkRandom(long seed) {
        super(seed);
        setSeed(tempSeed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeed(int[] seed) {
        setSeed(SeedUtils.convertToLong(seed));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setSeed(long seed) {
        if (this.seed != null) { //work around miserable override NPE in constructor.
            clear();
            this.seed.set(scrambleSeed(seed));
        } else {
            tempSeed = seed;
        }
    }

    /**
     * Returns the seed used by this generator.
     *
     * @return the seed (which can be used with {@link #setSeed(long)}.
     */
    public long getSeed() {
        return scrambleSeed(seed.get());
    }

    private static long scrambleSeed(long seed) {
        return (seed ^ MULTIPLIER) & MASK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int next(int bits) {
        long oldSeed, nextSeed;
        AtomicLong seed = this.seed;
        do {
            oldSeed = seed.get();
            nextSeed = (oldSeed * MULTIPLIER + ADDEND) & MASK;
        } while (!seed.compareAndSet(oldSeed, nextSeed));
        return (int) (nextSeed >>> (48 - bits));
    }

    /**
     * Reverse of {@link #next(int)}.
     * The method {@code prev} is implemented by class {@code ReversibleJdkRandom} by
     * atomically updating the seed to:
     * <pre>{@code ((seed - 0xBL) * 0xDFE05BCB1365L) & ((1L << 48) - 1)}</pre>
     * and returning:
     * <pre>{@code (int)(seed >>> (48 - bits))}.</pre>
     *
     * @param bits random bits.
     * @return the previous pseudorandom value from this random number
     * generator's sequence.
     */
    // https://github.com/votadlos/JavaCG/blob/master/JavaCG/JavaCG/JavaLCGMimic.cpp#L15
    protected int prev(int bits) {
        final AtomicLong seed = this.seed;
        long nextSeed, prevSeed;
        do {
            nextSeed = seed.get();
            prevSeed = ((nextSeed - ADDEND) * INVERSE_MULTIPLIER) & MASK;
        } while (!seed.compareAndSet(nextSeed, prevSeed));
        // we generate the bits from the current seed,
        // but we have updated the value of the seed with the prev one
        return (int) (nextSeed >>> (48 - bits));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prevBytes(byte[] bytes) {
        final int bytesInInt = Integer.SIZE / Byte.SIZE;
        final int remainder = bytes.length % bytesInInt;
        if (remainder > 0) {
            for (int i = remainder - 1,
                 rnd = prevInt();
                 i >= 0; i--, rnd >>= Byte.SIZE) {
                bytes[i] = (byte) (rnd);
            }
        }
        for (int i = remainder, len = bytes.length; i < len; ) {
            for (int rnd = prevInt(),
                 n = bytesInInt;
                 n-- > 0; rnd <<= Byte.SIZE) {
                bytes[i++] = (byte) (rnd >>> 24);
            }
        }
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
     */
    @Override
    public int prevInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        if ((bound & -bound) == bound) {
            return (int) (((long) bound * prev(31)) >> 31);
        }

        int j, k;
        do {// limit is not power of 2
            j = prev(31);// (seed >> 17)
            k = j % bound; // output is(seed >> 17) modulo limit
        } while ((j - k) + (bound - 1) < 0); // remove statistical bias
        return k;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long prevLong() {
        return prev(32) + ((long) (prev(32)) << 32);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean prevBoolean() {
        return prev(1) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float prevFloat() {
        return prev(24) * 0x1.0p-24f;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double prevDouble() {
        return (prev(27) + ((long) (prev(26)) << 27)) * DOUBLE_UNIT;
    }

    private void clear() {
        shouldReverseGaussian = false;
        if (hasNextGaussian) {
            super.nextGaussian();
            hasNextGaussian = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double nextGaussian() {
        shouldReverseGaussian = !shouldReverseGaussian;
        hasNextGaussian = !hasNextGaussian;
        return super.nextGaussian();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoNextGaussian() {
        if (shouldReverseGaussian) {
            double v1, v2, s;
            do {
                v1 = 2 * prevDouble() - 1; // between -1 and 1
                v2 = 2 * prevDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            clear();
        } else {
            shouldReverseGaussian = true;
        }
    }
}
