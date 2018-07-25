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

import org.apache.commons.math3.random.RandomGenerator;

import java.io.Serializable;

/**
 * This interface contains methods for generating random numbers backwards.
 * This means, that it can be used to find out the numbers generated previously.
 * The idea is that if you reverse all your steps and call the methods below,
 * you most likely get the same numbers that where generated before.
 */
public interface ReverseRandomGenerator extends RandomGenerator, Serializable {
    /**
     * The reverse of {@link #nextBytes(byte[])}.
     *
     * @param bytes the non-null byte array in which to put the random byte.
     */
    void prevBytes(byte[] bytes);

    /**
     * The reverse of {@link #nextInt()}.
     *
     * @return the previous pseudorandom, uniformly distributed {@code int} value from this
     * random number generator's sequence.
     */
    int prevInt();

    /**
     * The reverse of {@link #nextInt(int)}.
     *
     * @param bound the bound on the random number to be returned. Must be positive.
     * @return the previous pseudorandom, uniformly distributed {@code int} value between
     * {@code 0} (inclusive) and {@code bound} (exclusive).
     */
    int prevInt(int bound);

    /**
     * The reverse of {@link #nextLong()}.
     *
     * @return the previous pseudorandom, uniformly distributed {@code long} value from this
     * random number generator's sequence.
     */
    long prevLong();

    /**
     * The reverse of {@link #nextBoolean()}.
     *
     * @return the previous pseudorandom, uniformly distributed {@code boolean} value from this
     * random number generator's sequence.
     */
    boolean prevBoolean();

    /**
     * The reverse of {@link #nextFloat()}.
     *
     * @return the previous pseudorandom, uniformly distributed {@code float} value between
     * {@code 0.0} and {@code 1.0} from this random number generator's sequence.
     */
    float prevFloat();

    /**
     * The reverse of {@link #nextDouble()}.
     *
     * @return the previous pseudorandom, uniformly distributed {@code double} value between
     * {@code 0.0} and {@code 1.0} from this random number generator's sequence.
     */
    double prevDouble();

    /**
     * This method undoes the effect of {@link #nextGaussian()}. It is not possible to generate
     * the previous gaussian sequence in the same fashion as for the other methods, but we can
     * reverse the effect of calling the {@link #nextGaussian()}.
     */
    void undoNextGaussian();

    /**
     * Returns a random integer that is within a specified range.
     *
     * @param origin the least value, unless greater than bound
     * @param bound  the upper bound (exclusive), must not equal origin
     * @return a pseudorandom value
     */
    int nextInt(int origin, int bound);

    /**
     * Returns a random integer that is within a specified range.
     *
     * @param origin the least value, unless greater than bound
     * @param bound  the upper bound (exclusive), must not equal origin
     * @return the previous pseudorandom value
     */
    int prevInt(int origin, int bound);

    /**
     * Returns a pseudorandom, uniformly distributed {@code long} value
     * between 0 (inclusive) and the specified value (exclusive), drawn from
     * this random number generator's sequence.
     *
     * @param bound the bound on the random number to be returned.  Must be
     *              positive.
     * @return a pseudorandom, uniformly distributed {@code long}
     * value between {@code 0} (inclusive) and {@code bound} (exclusive).
     */
    long nextLong(long bound);

    /**
     * The reverse of {@link #nextLong(long)}.
     *
     * @param bound the bound on the random number to be returned. Must be positive.
     * @return the previous pseudorandom, uniformly distributed {@code long} value between
     * {@code 0} (inclusive) and {@code bound} (exclusive).
     */
    long prevLong(long bound);

//    long nextLong(long origin, long bound);
//
//    long prevLong(long origin, long bound);
}
