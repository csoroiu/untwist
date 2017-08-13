package ro.derbederos.untwist;

/**
 * Java implementation of the random number generator from Turbo Pascal 7/Delphi.
 * It is a <a href="https://en.wikipedia.org/wiki/Linear_congruential_generator">Linear congruential generator</a>
 * and the next seed is computed using the formula: <code>seed = (seed * 0x08088405 + 1) &amp; 0xFFFFFFFF</code>.
 * <p>
 * In Turbo Pascal 7 there was a switch that was enabling the use of the coprocessor.
 * There is a difference of <code>0.5</code> when generating a random floating number when
 * coprocessor was disabled <code>{$N-}</code> or enabled <code>{$N+}</code>.
 * Delphi maintains compatibility with the <code>{$N-}</code> version.
 * When enabling coprocessor <code>{$N+}</code>, in Turbo Pascal 7, the values returned by random
 * function are offset-ed by <code>0.5</code> from the usual ones. If value is <code>&gt;=0.5</code>
 * then it is decreased by <code>0.5</code> else value is increased by <code>0.5</code>.
 * This seems to be a design fault, as 32 bit numbers are emulated in Turbo Pascal 7.
 */
public class TurboPascalRandom extends ReverseBitsStreamGenerator {
    private static final long serialVersionUID = 1L;

    private final static long multiplier = 0x08088405L;
    private final static long invmultiplier = 0xD94FA8CDL;
    private final static long addend = 0x1L;
    private final static long mask = (1L << 32) - 1;

    private final boolean coprocEnabled;

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
     * @param seed          the initial seed (32 bits integer).
     * @param coprocEnabled flag that enables the offsetting by <code>0.5</code> of the generated floating number.
     * @see #setSeed(int)
     */
    protected TurboPascalRandom(int seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
    }

    /**
     * Creates a new random number generator using an int array seed.
     *
     * @param seed          the initial seed (32 bits integers array).
     * @param coprocEnabled flag that enables the offsetting by <code>0.5</code> of the generated floating number.
     * @see #setSeed(int[])
     */
    protected TurboPascalRandom(int[] seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
    }

    /**
     * Creates a new random number generator using a single long seed.
     *
     * @param seed          the initial seed (64 bits integer).
     * @param coprocEnabled flag that enables the offsetting by <code>0.5</code> of the generated floating number.
     * @see #setSeed(long)
     */
    protected TurboPascalRandom(long seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
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
     * Converts the <code>int[]</code> seed to an <code>int</code> and calls {@link #setSeed(int)}.
     *
     * @param seed an array used to calculate a starting value for the pseudo-random number sequence.
     * @see RandomUtils#convertToInt(int...)
     */
    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToInt(seed));
    }

    /**
     * Converts the <code>long</code> seed to an <code>int</code> and calls {@link #setSeed(int)}.
     *
     * @param seed a number used to calculate a starting value for the pseudo-random number sequence.
     * @see RandomUtils#convertToInt(int, int)
     */
    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        final int low = (int) (seed & 0xFFFFFFFFL);
        setSeed(RandomUtils.convertToInt(high, low));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int next(int bits) {
        nextSeed();
        return (int) (seed >>> 32 - bits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int prev(int bits) {
        int result = (int) (seed >>> 32 - bits);
        prevSeed();
        return result;
    }

    private void nextSeed() {
        seed = (seed * multiplier + addend) & mask;
    }

    private void prevSeed() {
        seed = ((seed - addend) * invmultiplier) & mask;
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
        if (coprocEnabled) {
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
        if (coprocEnabled) {
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
