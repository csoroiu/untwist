/*
 * Copyright (c) 2017-2019 Claudiu Soroiu
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
 * The current java implementation is based on the source code:
 * <a href="https://github.com/graemeg/freepascal/blob/3cf1b72/rtl/inc/system.inc#L539">System.inc</a>.
 * <p>
 * The issue fixed is #35878: <a href="https://bugs.freepascal.org/view.php?id=35878">
 * 0035878: Random numbers provided by "function Random(l:int64):int64;" are not equidistributed.</a>
 */
public class FreePascal32Random extends FreePascalRandom {
    /**
     * Creates a new random number generator.
     * <p>
     * The instance is initialized using the current time plus the
     * system identity hash code of this instance as the seed.
     */
    public FreePascal32Random() {
        super();
    }

    /**
     * Creates a new random number generator using a single int seed.
     *
     * @param seed the initial seed (32 bits integer).
     * @see #setSeed(int)
     */
    public FreePascal32Random(int seed) {
        this(new int[]{seed});
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed the initial seed (32 bits integers array), if null
     *             the seed of the generator will be related to the current time.
     * @see #setSeed(int[])
     */
    public FreePascal32Random(int[] seed) {
        super(seed);
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed the initial seed (64 bits integer).
     * @see #setSeed(long)
     */
    public FreePascal32Random(long seed) {
        super(seed);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/3cf1b72/rtl/inc/system.inc#L684">System.inc#random(int64)</a>
     */
    @Override
    public long nextLong(long bound) throws IllegalArgumentException {
        if (bound < 0) {
            bound++;
        }
        long q = bound;
        if (Long.compareUnsigned(q, bound & 0xFFFFFFFFL) > 0) {
            q = -bound;
        }
        long a = Integer.toUnsignedLong(next());
        long b = Integer.toUnsignedLong(next());
        long c = q >>> 32;
        long d = q & 0xFFFFFFFFL;

        long bd = b * d;
        long ad = a * d;
        long bc = b * c;
        long ac = a * c;

        long carry = ((bd >>> 32L) + (ad & 0xFFFFFFFFL) + (bc & 0xFFFFFFFFL)) >>> 32;

        long result = carry + (ad >>> 32) + (bc >>> 32) + ac;
        return (bound < 0) ? -result : result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Source code:
     * <a href="https://github.com/graemeg/freepascal/blob/3cf1b72/rtl/inc/system.inc#L684">System.inc#random(int64)</a>
     */
    @Override
    public long prevLong(long bound) throws IllegalArgumentException {
        if (bound < 0) {
            bound++;
        }
        long q = bound;
        if (Long.compareUnsigned(q, bound & 0xFFFFFFFFL) > 0) {
            q = -bound;
        }
        long b = Integer.toUnsignedLong(prev());
        long a = Integer.toUnsignedLong(prev());
        long c = q >>> 32;
        long d = q & 0xFFFFFFFFL;

        long bd = b * d;
        long ad = a * d;
        long bc = b * c;
        long ac = a * c;

        long carry = ((bd >>> 32L) + (ad & 0xFFFFFFFFL) + (bc & 0xFFFFFFFFL)) >>> 32;

        long result = carry + (ad >>> 32) + (bc >>> 32) + ac;
        return (bound < 0) ? -result : result;
    }
}
