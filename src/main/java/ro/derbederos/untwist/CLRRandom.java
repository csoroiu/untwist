package ro.derbederos.untwist;

import org.apache.commons.math3.random.BitsStreamGenerator;

import java.util.Arrays;

public class CLRRandom implements ReverseRandomGenerator {
    private static final long serialVersionUID = 1L;

    // http://referencesource.microsoft.com/#mscorlib/system/random.cs
    // https://github.com/dotnet/coreclr/blob/master/src/mscorlib/shared/System/Random.cs
    // tests
    // https://github.com/dotnet/coreclr/tree/master/tests/src/CoreMangLib/cti/system/random
    private static final int MBIG = Integer.MAX_VALUE;
    private static final int MSEED = 161803398;

    private int inext;
    private int inextp;
    private int[] seedArray = new int[56];
    private double nextGaussian = Double.NaN;

    public CLRRandom() {
        setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    public CLRRandom(int seed) {
        setSeed(seed);
    }

    public CLRRandom(int[] seed) {
        setSeed(seed);
    }

    public CLRRandom(long seed) {
        setSeed(seed);
    }

    private void initialize(int seed) {
        int ii = 0;
        int mj, mk;

        // Initialize our seed array.
        // This algorithm comes from Numerical Recipes in C (2nd Ed.)
        int subtraction = (seed == Integer.MIN_VALUE) ? Integer.MAX_VALUE : Math.abs(seed);
        mj = MSEED - subtraction;
        seedArray[55] = mj;
        mk = 1;
        for (int i = 1; i < 55; i++) {
            // Apparently the range [1..55] is special (Knuth) and so we're wasting the 0'th position.
            if ((ii += 21) >= 55) ii -= 55;
            seedArray[ii] = mk;
            mk = mj - mk;
            if (mk < 0) {
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
                if (seedArray[i] < 0) {
                    seedArray[i] += MBIG;
                }
            }
        }
        //.net initializes this with 0, but ++0 = ++55 (according to internalSample)
        // 55 helps us for the prevInternalSample
        inext = 55;
        inextp = 21;
        nextGaussian = Double.NaN;
    }

    //
    // Package Private Methods
    //

    /**
     * Return a new random number [0..1) and re-seed the seed array.
     *
     * @return a double [0..1)
     */
    private double sample() {
        // Including this division at the end gives us significantly improved
        // random number distribution.
        return internalSample() * (1.0 / MBIG);
    }

    /**
     * Return a new random number [0..1) and re-seed the seed array.
     *
     * @return a double [0..1)
     */
    private double prevSample() {
        // Including this division at the end gives us significantly improved
        // random number distribution.
        return prevInternalSample() * (1.0 / MBIG);
    }

    private int internalSample() {
        int retVal;
        int locINext = inext;
        int locINextp = inextp;

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
        if (retVal < 0) {
            retVal += MBIG;
        }

        seedArray[locINext] = retVal;

        inext = locINext;
        inextp = locINextp;

        return retVal;
    }

    private int prevInternalSample() {
        int retVal;
        int locINext = inext;
        int locINextp = inextp;

        retVal = seedArray[locINext];

        int prevRetVal = seedArray[locINextp] + retVal;
        if (prevRetVal == MBIG) {
            prevRetVal--;
        }
        if (prevRetVal < 0) {
            prevRetVal -= MBIG;
        }
        seedArray[locINext] = prevRetVal;

        if (--locINext < 1) {
            locINext = 55;
        }
        if (--locINextp == 0) {
            locINextp = 55;
        }

        inext = locINext;
        inextp = locINextp;
        return retVal;
    }

    int[] getState() {
        return Arrays.copyOf(seedArray, seedArray.length);
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
        d += (Integer.MAX_VALUE - 1); // get a number in range [0 .. 2 * Int32MaxValue - 1)
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
        d += (Integer.MAX_VALUE - 1); // get a number in range [0 .. 2 * Int32MaxValue - 1)
        d /= 2 * (long) Integer.MAX_VALUE - 1;
        return d;
    }

    //
    // Public Instance Methods
    //

    @Override
    public void setSeed(int seed) {
        initialize(seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToInt(seed));
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        final int low = (int) (seed & 0xffffffffL);
        setSeed(RandomUtils.convertToInt(high, low));
    }

    /**
     * @return an int [0..Int32.MaxValue)
     */
    @Override
    public int nextInt() {
        return internalSample();
    }

    /**
     * @return an int [0..Int32.MaxValue)
     */
    @Override
    public int prevInt() {
        return prevInternalSample();
    }

    /**
     * @param minValue the least legal value for the Random number.
     * @param maxValue one greater than the greatest legal return value.
     * @return an int [minvalue..maxvalue)
     */
    public int nextInt(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }

        long range = (long) maxValue - minValue;
        if (range <= (long) Integer.MAX_VALUE) {
            return ((int) (sample() * range) + minValue);
        } else {
            return (int) ((long) (getSampleForLargeRange() * range) + minValue);
        }
    }

    /**
     * @param minValue the least legal value for the Random number.
     * @param maxValue one greater than the greatest legal return value.
     * @return an int [minvalue..maxvalue)
     */
    public int prevInt(int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("minValue must be less than maxValue");
        }

        long range = (long) maxValue - minValue;
        if (range <= (long) Integer.MAX_VALUE) {
            return ((int) (sample() * range) + minValue);
        } else {
            return (int) ((long) (getPrevSampleForLargeRange() * range) + minValue);
        }
    }

    /**
     * @param maxValue one more than the greatest legal return value.
     * @return an int [0..maxValue)
     */
    @Override
    public int nextInt(int maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue must be positive");
        }
        return (int) (sample() * maxValue);
    }

    /**
     * @param maxValue one more than the greatest legal return value.
     * @return an int [0..maxValue)
     */
    @Override
    public int prevInt(int maxValue) {
        if (maxValue < 0) {
            throw new IllegalArgumentException("maxValue must be positive");
        }
        return (int) (prevSample() * maxValue);
    }

    /**
     * @return a double [0..1)
     */
    @Override
    public double nextDouble() {
        return sample();
    }

    /**
     * @return a double [0..1)
     */
    @Override
    public double prevDouble() {
        return prevSample();
    }

    @Override
    @Deprecated
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override
    @Deprecated
    public float prevFloat() {
        return (float) prevDouble();
    }

    /**
     * Fills the byte array with random bytes [0..0x7f]. The entire array is filled.
     *
     * @param buffer the array to be filled.
     */
    @Override
    public void nextBytes(byte[] buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) (internalSample() % (1 << 8));
        }
    }

    /**
     * Fills the byte array with random bytes [0..0x7f]. The entire array is filled.
     *
     * @param buffer the array to be filled.
     */
    @Override
    public void prevBytes(byte[] buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) (prevInternalSample() % (1 << 8));
        }
    }

    @Override
    public long nextLong() {
        return ((long) (nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE)) << 32)
                | ((long) nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE)) & 0xFFFFFFFFL;
    }

    @Override
    public long prevLong() {
        return ((long) prevInt(Integer.MIN_VALUE, Integer.MAX_VALUE)) & 0xFFFFFFFFL |
                ((long) (prevInt(Integer.MIN_VALUE, Integer.MAX_VALUE)) << 32);
    }

    @Override
    public boolean nextBoolean() {
        return sample() >= 0.5; // same thing as nextInt(2) == 1
    }

    @Override
    public boolean prevBoolean() {
        return prevSample() >= 0.5; // same thing as nextInt(2) == 1
    }

    /**
     * {@link BitsStreamGenerator#nextGaussian}
     */
    @Override
    public double nextGaussian() {
        final double random;
        if (Double.isNaN(nextGaussian)) {
            // generate a new pair of gaussian numbers
            final double x = nextDouble();
            final double y = nextDouble();
            final double alpha = 2 * Math.PI * x;
            final double r = Math.sqrt(-2 * Math.log(y));
            random = r * Math.cos(alpha);
            nextGaussian = r * Math.sin(alpha);
        } else {
            // use the second element of the pair already generated
            random = nextGaussian;
            nextGaussian = Double.NaN;
        }
        return random;
    }

    @Override
    public double prevGaussian() {
        //FIXME
        throw new UnsupportedOperationException();
    }
}
