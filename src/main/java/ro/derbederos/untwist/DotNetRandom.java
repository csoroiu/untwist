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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A Java implementation of the .NET random number generator.
 * <p>
 * .Net Documentation states: The current implementation of the Random class
 * is based on a modified version of Donald E. Knuth's subtractive random number
 * generator algorithm. For more information, see D. E. Knuth. The Art of
 * Computer Programming, Volume 2: Seminumerical Algorithms. Addison-Wesley,
 * Reading, MA, third edition, 1997.
 * <p>
 * This algorithm comes from Numerical Recipes in C (2nd Ed.) with a bug.
 * <p>
 * Quote from the issue reported:
 * <i>'Knuth was very specific about the lagged Fibonacci sequence coefficient
 * (24 and 55). Somehow Microsoft mistyped the value to be 21 (this.iNextp = 0x15
 * in public Random(int Seed) in source code),  in place of 31 (=55-24). Due to
 * this mistype, the random number generated no longer has the guarantee on the
 * period to be longer than (2^55-1). The Knuth version of lags (24, 55) has been
 * tested by many people about the property of the generated numbers but not the
 * one created by Microsoft.'</i>
 * <p>
 * Source code:
 * <ul>
 * <li><a href="https://docs.microsoft.com/en-us/dotnet/api/system.random?view=netcore-3.0">
 * .NET Framework Random class Reference Source</a></li>
 * <li><a href="https://github.com/dotnet/corefx/blob/master/src/Common/src/CoreLib/System/Random.cs">
 * .NET Core Random class source</a></li>
 * <li><a href="https://github.com/dotnet/coreclr/tree/f7f0929a46/tests/src/CoreMangLib/cti/system/random">
 * .NET Core Random class tests (before moving to corefx)</a></li>
 * <li><a href="https://github.com/dotnet/corefx/blob/master/src/System.Runtime.Extensions/tests/System/Random.cs">
 * .NET CoreFx Random class tests</a></li>
 * </ul>
 * <p>
 * Issues:
 * <ul>
 * <li><a href="https://connect.microsoft.com/VisualStudio/feedback/details/634761/system-random-serious-bug">
 * https://connect.microsoft.com/VisualStudio/feedback/details/634761/system-random-serious-bug</a></li>
 * <li><a href="https://github.com/dotnet/coreclr/issues/5974">
 * https://github.com/dotnet/coreclr/issues/5974</a></li>
 * <li><a href="https://github.com/dotnet/corefx/issues/12746">
 * https://github.com/dotnet/corefx/issues/12746</a></li>
 * </ul>
 */
public class DotNetRandom implements ReverseRandomGenerator {
    private static final long serialVersionUID = 1L;

    private static final int STATE_SIZE = 58 * Integer.BYTES; //seedArray, iNext, INextp

    private static final int MBIG = Integer.MAX_VALUE;
    private static final int MSEED = 161803398;
    private static final int MZ = 0;

    // the state of the generator
    private final int[] seedArray = new int[56];
    private int iNext;
    private int iNextp;
    private double nextGaussian = Double.NaN;
    private boolean shouldReverseGaussian;

    /**
     * Initializes a new instance of the {@link DotNetRandom} class, using a time-dependent default seed value.
     * <p>
     * The instance is initialized using the current time plus the system identity hash code of this instance as the seed.
     */
    public DotNetRandom() {
        setSeed((int) (System.currentTimeMillis() + System.identityHashCode(this)));
    }

    /**
     * Initializes a new instance of the {@link DotNetRandom} class, using the specified seed value.
     *
     * @param seed the initial seed.
     * @see #setSeed(int)
     */
    public DotNetRandom(int seed) {
        setSeed(seed);
    }

    /**
     * Initializes a new instance of the {@link DotNetRandom} class, using the specified seed value.
     *
     * @param seed the initial seed.
     * @see #setSeed(long)
     */
    public DotNetRandom(long seed) {
        setSeed(seed);
    }

    private void initialize(int seed) {
        int ii = 0;
        int mj, mk;

        // Initialize our seed array.
        // This algorithm comes from Numerical Recipes in C (2nd Ed.)
        int subtraction = seed == Integer.MIN_VALUE ? Integer.MAX_VALUE : Math.abs(seed);
        mj = MSEED - subtraction;
        seedArray[55] = mj;
        mk = 1;
        for (int i = 1; i < 55; i++) {
            // Apparently the range [1..55] is special (Knuth) and so we're wasting the 0'th position.
            if ((ii += 21) >= 55) ii -= 55;
            seedArray[ii] = mk;
            mk = mj - mk;
            if (mk < MZ) {
                mk += MBIG;
            }
            mj = seedArray[ii];
        }
        for (int k = 1; k < 5; k++) {
            for (int i = 1; i < 56; i++) {
                int n = i + 30;
                if (n >= 55) {
                    n -= 55;
                }
                seedArray[i] -= seedArray[1 + n];
                if (seedArray[i] < MZ) {
                    seedArray[i] += MBIG;
                }
            }
        }
        // .net initializes this with 0, but (++0) % 56 = (++55) % 56 (according to internalSample)
        // value 55 helps us for the prevInternalSample
        iNext = 55;
        iNextp = 21;
        nextGaussian = Double.NaN;
        shouldReverseGaussian = false;
    }

    //
    // Package Private Methods
    //

    /**
     * Returns a random floating-point number between {@code 0.0} and {@code 1.0}.
     *
     * @return A double-precision floating point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     */
    protected /*virtual*/ double sample() {
        // Including this division at the end gives us significantly improved
        // random number distribution.
        return internalSample() * (1.0 / MBIG);
    }

    /**
     * The reverse of {@link #sample()}.
     * <p>
     * Returns a random floating-point number between {@code 0.0} and {@code 1.0}.
     *
     * @return A double-precision floating point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     */
    protected /*virtual*/ double prevSample() {
        // Including this division at the end gives us significantly improved
        // random number distribution.
        return prevInternalSample() * (1.0 / MBIG);
    }

    private int internalSample() {
        int retVal;
        int locINext = iNext;
        int locINextp = iNextp;

        if (++locINext >= 56) {
            locINext = 1;
        }
        if (++locINextp >= 56) {
            locINextp = 1;
        }

        retVal = seedArray[locINext] - seedArray[locINextp];

        if (retVal == MBIG) {
            retVal--;
        }
        if (retVal < MZ) {
            retVal += MBIG;
        }

        seedArray[locINext] = retVal;

        iNext = locINext;
        iNextp = locINextp;

        return retVal;
    }

    private int prevInternalSample() {
        int retVal;
        int locINext = iNext;
        int locINextp = iNextp;

        retVal = seedArray[locINext];

        int prevRetVal = seedArray[locINextp] + retVal;
        if (prevRetVal == MBIG) {
            prevRetVal--;
        }
        if (prevRetVal < MZ) {
            prevRetVal -= MBIG;
        }
        seedArray[locINext] = prevRetVal;

        if (--locINext < 1) {
            locINext = 55;
        }
        if (--locINextp == 0) {
            locINextp = 55;
        }

        iNext = locINext;
        iNextp = locINextp;
        return retVal;
    }

    byte[] getState() {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(STATE_SIZE);
        DataOutputStream output = new DataOutputStream(byteOutput);
        try {
            output.writeInt(iNext);
            output.writeInt(iNextp);
            for (int value : seedArray) {
                output.writeInt(value);
            }
            output.close();
        } catch (IOException ignore) {
        }
        return byteOutput.toByteArray();
    }

    private double getSampleForLargeRange() {
        // The distribution of double value returned by Sample
        // is not distributed well enough for a large range.
        // If we use Sample for a range [Int32.MinValue..Int32.MaxValue)
        // We will end up getting even numbers only.

        int result = internalSample();
        // Note we can't use addition here. The distribution will be bad if we do that.
        boolean negative = internalSample() % 2 == 0; // decide the sign based on second sample
        if (negative) {
            result = -result;
        }
        double d = result;
        d += Integer.MAX_VALUE - 1; // get a number in range [0 .. 2 * Int32MaxValue - 1)
        d /= 2 * (long) Integer.MAX_VALUE - 1;
        return d;
    }

    private double getPrevSampleForLargeRange() {
        // The distribution of double value returned by Sample
        // is not distributed well enough for a large range.
        // If we use Sample for a range [Int32.MinValue..Int32.MaxValue)
        // We will end up getting even numbers only.

        // Note we can't use addition here. The distribution will be bad if we do that.
        boolean negative = prevInternalSample() % 2 == 0; // decide the sign based on second sample

        int result = prevInternalSample();
        if (negative) {
            result = -result;
        }
        double d = result;
        d += Integer.MAX_VALUE - 1; // get a number in range [0 .. 2 * Int32MaxValue - 1)
        d /= 2 * (long) Integer.MAX_VALUE - 1;
        return d;
    }

    //
    // Public Instance Methods
    //

    /**
     * Initializes this instance of the {@link DotNetRandom} class, using the specified seed value.
     *
     * @param seed a number used to calculate a starting value for the pseudo-random number sequence.
     *             If a negative number is specified, the absolute value of the number is used.
     */
    @Override
    public void setSeed(int seed) {
        initialize(seed);
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
     * Returns a non-negative random integer.
     * <p>
     * <font color="red">This violates the contract of {@link RandomGenerator#nextInt()}.</font>
     *
     * @return A 32-bit signed integer that is greater than or equal to {@code 0}
     * and less than {@code System.Int32.MaxValue (Integer.MAX_VALUE)}.
     * @see RandomGenerator#nextInt()
     */
    @Override
    public int nextInt() {
        return internalSample();
    }

    /**
     * The reverse of {@link #nextInt()}.
     * <p>
     * Returns a non-negative random integer.
     * <p>
     * <font color="red">This violates the contract of {@link ReverseRandomGenerator#prevInt()}</font>
     *
     * @return A 32-bit signed integer that is greater than or equal to {@code 0}
     * and less than {@code System.Int32.MaxValue (Integer.MAX_VALUE)}.
     * @see RandomGenerator#nextInt()
     * @see ReverseRandomGenerator#prevInt()
     */
    @Override
    public int prevInt() {
        return prevInternalSample();
    }

    /**
     * Returns a random integer that is within a specified range.
     *
     * @param minValue the inclusive lower bound of the random number returned.
     * @param maxValue the exclusive upper bound of the random number returned.
     *                 {@code maxValue} must be greater than or equal to {@code minValue}.
     * @return a 32-bit signed integer greater than or equal to {@code minValue} and less than
     * {@code maxValue}; that is, the range of return values includes {@code minValue} but
     * not {@code maxValue}. If {@code minValue} equals {@code maxValue},
     * {@code minValue} is returned.
     */
    @Override
    public int nextInt(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }

        long range = (long) maxValue - minValue;
        if (range <= (long) Integer.MAX_VALUE) {
            return (int) (sample() * range) + minValue;
        } else {
            return (int) ((long) (getSampleForLargeRange() * range) + minValue);
        }
    }

    /**
     * The reverse of {@link #nextInt(int, int)}.
     * <p>
     * Returns a random integer that is within a specified range.
     *
     * @param minValue the inclusive lower bound of the random number returned.
     * @param maxValue the exclusive upper bound of the random number returned.
     *                 {@code maxValue} must be greater than or equal to {@code minValue}.
     * @return a 32-bit signed integer greater than or equal to {@code minValue} and less than
     * {@code maxValue}; that is, the range of return values includes {@code minValue} but
     * not {@code maxValue}. If {@code minValue} equals {@code maxValue},
     * {@code minValue} is returned.
     */
    @Override
    public int prevInt(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }

        long range = (long) maxValue - minValue;
        if (range <= (long) Integer.MAX_VALUE) {
            return (int) (prevSample() * range) + minValue;
        } else {
            return (int) ((long) (getPrevSampleForLargeRange() * range) + minValue);
        }
    }

    /**
     * Returns a non-negative random integer that is less than the specified maximum.
     *
     * @param maxValue the exclusive upper bound of the random number to be generated. {@code maxValue} must
     *                 be greater than or equal to {@code 0}.
     * @return A 32-bit signed integer that is greater than or equal to {@code 0}, and less than {@code maxValue};
     * that is, the range of return values ordinarily includes {@code 0} but not {@code maxValue}. However,
     * if {@code maxValue} equals {@code 0}, {@code maxValue} is returned.
     */
    @Override
    public int nextInt(int maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue must be positive");
        }
        return (int) (sample() * maxValue);
    }

    /**
     * The reverse of {@link #nextInt(int)}.
     * <p>
     * Returns a non-negative random integer that is less than the specified maximum.
     *
     * @param maxValue the exclusive upper bound of the random number to be generated. {@code maxValue} must
     *                 be greater than or equal to {@code 0}.
     * @return A 32-bit signed integer that is greater than or equal to {@code 0}, and less than {@code maxValue};
     * that is, the range of return values ordinarily includes {@code 0} but not {@code maxValue}. However,
     * if {@code maxValue} equals 0, {@code maxValue} is returned.
     */
    @Override
    public int prevInt(int maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue must be positive");
        }
        return (int) (prevSample() * maxValue);
    }

    /**
     * Returns a random floating-point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     *
     * @return A double-precision floating point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     */
    @Override
    public double nextDouble() {
        return sample();
    }

    /**
     * The reverse of {@link #nextDouble()}.
     * <p>
     * Returns a random floating-point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     *
     * @return A double-precision floating point number that is greater than or equal to {@code 0.0},
     * and less than {@code 1.0}.
     */
    @Override
    public double prevDouble() {
        return prevSample();
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
     * Generates random bytes and places them into a user-supplied array.
     * <p>
     * The array is filled with bytes extracted from random integers.
     * This implies that the number of random bytes generated may be larger than
     * the length of the byte array.
     * </p>
     *
     * @param bytes the non-null byte array in which to put the random bytes.
     * @param start index at which to start inserting the generated bytes.
     * @param len   number of bytes to insert.
     */
    public void nextBytes(byte[] bytes, int start, int len) {
        if (start < 0 || start >= bytes.length) {
            throw new IndexOutOfBoundsException(start + " is out of interval [" + 0 + ", " + bytes.length + ")");
        }
        if (len < 0 || len > bytes.length - start) {
            throw new IndexOutOfBoundsException(len + " is out of interval [" + 0 + ", " + (bytes.length - start) + "]");
        }

        nextBytesFill(bytes, start, len);
    }

    /**
     * Fills the elements of a specified array of bytes with random numbers.
     *
     * @param bytes an array of bytes to contain random numbers.
     */
    @Override
    public void nextBytes(byte[] bytes) {
        nextBytesFill(bytes, 0, bytes.length);
    }

    private void nextBytesFill(byte[] bytes, int start, int len) {
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }

        for (int i = start; i < start + len; i++) {
            bytes[i] = (byte) (nextInt() % (1 << 8));
        }
    }

    /**
     * The reverse of {@link #nextBytes(byte[], int, int)}.
     *
     * @param bytes the non-null byte array in which to put the random bytes.
     * @param start index at which to start inserting the generated bytes.
     * @param len   number of bytes to insert.
     */
    public void prevBytes(byte[] bytes, int start, int len) {
        if (start < 0 || start >= bytes.length) {
            throw new IndexOutOfBoundsException(start + " is out of interval [" + 0 + ", " + bytes.length + ")");
        }
        if (len < 0 || len > bytes.length - start) {
            throw new IndexOutOfBoundsException(len + " is out of interval [" + 0 + ", " + (bytes.length - start) + "]");
        }

        prevBytesFill(bytes, start, len);
    }

    /**
     * The reverse of {@link #nextBytes(byte[])}.
     * <p>
     * Fills the elements of a specified array of bytes with random numbers.
     *
     * @param bytes an array of bytes to contain random numbers.
     */
    @Override
    public void prevBytes(byte[] bytes) {
        prevBytesFill(bytes, 0, bytes.length);
    }

    private void prevBytesFill(byte[] bytes, int start, int len) {
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }

        for (int i = start; i < start + len; i++) {
            bytes[i] = (byte) (prevInt() % (1 << 8));
        }
    }

    /**
     * Returns a 64 bit random integer (long). Unlike {@link #nextInt()} all 2<sup>64</sup> possible
     * {@code long} values should be produced with (approximately) equal probability.
     * <p>
     * <b>This method is not part of .Net implementation.</b>
     *
     * @return a 64 bit random integer (long).
     */
    @Override
    public long nextLong() {
        long b1 = nextInt() & 0xFFFFL; // 16 bits
        long b2 = nextInt() & 0xFFFFFFL; // 24 bits
        long b3 = nextInt() & 0xFFFFFFL; // 24 bits
        return b1 << 48 | b2 << 24 | b3;
    }

    /**
     * The reverse of {@link #nextLong()}.
     * <p>
     * Returns a 64 bit random integer (long). Unlike {@link #prevInt()}, all 2<sup>64</sup> possible
     * {@code long} values should be produced with (approximately) equal probability.
     *
     * @return a 64 bit random integer (long).
     */
    @Override
    public long prevLong() {
        long b3 = prevInt() & 0xFFFFFFL; // 24 bits
        long b2 = prevInt() & 0xFFFFFFL; // 24 bits
        long b1 = prevInt() & 0xFFFFL; // 16 bits
        return b1 << 48 | b2 << 24 | b3;
    }

    /**
     * {@inheritDoc}
     *
     * @implNote <b>This method is not part of .Net implementation.</b>
     */
    @Override
    public long nextLong(long maxValue) {
        return DefaultRandomPrimitivesFactory.nextLong(this, maxValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long prevLong(long maxValue) {
        return DefaultRandomPrimitivesFactory.prevLong(this, maxValue);
    }

    /**
     * Returns a random boolean.
     * Uses the formula {@code nextInt(2) == 1}.
     *
     * @return a random boolean.
     */
    @Override
    public boolean nextBoolean() {
        return DefaultRandomPrimitivesFactory.toBoolean(nextInt(2));
    }

    /**
     * The reverse of {@link #nextBoolean()}.
     * <p>
     * Returns a random boolean.
     * Uses the formula {@code nextInt(2) == 1}.
     *
     * @return a random boolean.
     */
    @Override
    public boolean prevBoolean() {
        return DefaultRandomPrimitivesFactory.toBoolean(prevInt(2));
    }

    /**
     * {@inheritDoc}
     * <p>
     * It uses <a href="https://en.wikipedia.org/wiki/Box%E2%80%93Muller_transform#Implementation">Box-Müller transform</a>.
     *
     * @implNote <b>This method is not part of .Net implementation.</b>
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
            final double r = StrictMath.sqrt(-2 * StrictMath.log(y));
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
            nextGaussian = Double.NaN;
            shouldReverseGaussian = false;
        } else {
            shouldReverseGaussian = true;
        }
    }
}
