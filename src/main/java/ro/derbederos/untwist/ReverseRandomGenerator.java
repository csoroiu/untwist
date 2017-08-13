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
     * @return the previous pseudorandom, uniformly distributed <code>int</code> value from this
     * random number generator's sequence.
     */
    int prevInt();

    /**
     * The reverse of {@link #nextInt(int)}.
     *
     * @param bound the bound on the random number to be returned. Must be positive.
     * @return the previous pseudorandom, uniformly distributed <code>int</code> value between
     * <code>0</code> (inclusive) and <code>bound</code> (exclusive).
     */
    int prevInt(int bound);

    /**
     * The reverse of {@link #nextLong()}.
     *
     * @return the previous pseudorandom, uniformly distributed <code>long</code> value from this
     * random number generator's sequence.
     */
    long prevLong();

    /**
     * The revese of {@link #nextBoolean()}.
     *
     * @return the previous pseudorandom, uniformly distributed <code>boolean</code> value from this
     * random number generator's sequence.
     */
    boolean prevBoolean();

    /**
     * The revese of {@link #nextFloat()}.
     *
     * @return the previous pseudorandom, uniformly distributed <code>float</code> value between
     * <code>0.0</code> and <code>1.0</code> from this random number generator's sequence.
     */
    float prevFloat();

    /**
     * The revese of {@link #nextDouble()}.
     *
     * @return the previous pseudorandom, uniformly distributed <code>double</code> value between
     * <code>0.0</code> and <code>1.0</code> from this random number generator's sequence.
     */
    double prevDouble();

    /**
     * This method undoes the effect of {@link #nextGaussian()}. It is not possible to generate
     * the previous gaussian sequence in the same fashion as for the other methods, but we can
     * reverse the effect of calling the {@link #nextGaussian()}.
     */
    void undoNextGaussian();
}
