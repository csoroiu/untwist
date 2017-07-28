package ro.sixi.eval.random;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class ReversibleJdkRandom extends Random implements ReverseRandomGenerator {
    private static final long serialVersionUID = 5566331316770172904L;

    private final static long multiplier = 0x5DEECE66DL;
    private final static long invmultiplier = 0xDFE05BCB1365L;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;
    private final static double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    private AtomicLong seedRef;

    public ReversibleJdkRandom(long seed) {
        super(seed);
        setReferenceToSeed();
    }

    public ReversibleJdkRandom() {
        super();
        setReferenceToSeed();
    }

    private void setReferenceToSeed() {
        try {
            Field f = Random.class.getDeclaredField("seed");
            f.setAccessible(true);
            seedRef = (AtomicLong) f.get(this);
        } catch (Exception e) {
        }
    }

    public long getSeed() {
        return (seedRef.get() ^ multiplier) & mask;
    }

    // https://github.com/votadlos/JavaCG/blob/master/JavaCG/JavaCG/JavaLCGMimic.cpp#L15
    protected int prev(int bits) {
        final AtomicLong seed = this.seedRef;
        long nextSeed, prevSeed;
        do {
            nextSeed = seed.get();
            prevSeed = (invmultiplier * (nextSeed - addend)) & mask;
        } while (!seed.compareAndSet(nextSeed, prevSeed));
        // we generate the bits from the current seed,
        // but we have updated the value of the seed with the prev one
        return (int) (nextSeed >>> (48 - bits));
    }

    @Override
    public void prevBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len; ) {
            for (int rnd = prevInt(), n = Math.min(len - i, Integer.SIZE / Byte.SIZE); n-- > 0; rnd <<= Byte.SIZE) {
                bytes[i++] = (byte) (rnd >>> 24);
            }
        }
    }

    /**
     * Works as a complete reverse of nextBytes. It will drop the first bits of the prevInt, and not the last.
     */
    @Override
    public void prevBytesMirror(byte[] bytes) {
        final int bytesInInt = Integer.SIZE / Byte.SIZE;
        final int remainder = bytes.length % bytesInInt;
        if (remainder > 0) {
            for (int i = remainder - 1, rnd = prevInt(); i >= 0; i--, rnd >>= Byte.SIZE) {
                bytes[i] = (byte) (rnd);
            }
        }
        for (int i = remainder, len = bytes.length; i < len; ) {
            for (int rnd = prevInt(), n = bytesInInt; n-- > 0; rnd <<= Byte.SIZE) {
                bytes[i++] = (byte) (rnd >>> 24);
            }
        }
    }

    @Override
    public int prevInt() {
        return prev(32);
    }

    @Override
    public int prevInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        if ((bound & -bound) == bound) // limit is power of 2
            return (int) (((long) bound * prev(31)) >> 31); // output limit*(seed >> 17) >> 31

        int j, k;
        do {// limit is not power of 2
            j = prev(31);// (seed >> 17)
            k = j % bound; // output is(seed >> 17) modulo limit
        } while ((j - k) + (bound - 1) < 0); // remove statistical bias
        return k;
    }

    @Override
    public long prevLong() {
        return prev(32) + ((long) (prev(32)) << 32);
    }

    @Override
    public boolean prevBoolean() {
        return prev(1) != 0;
    }

    @Override
    public float prevFloat() {
        return prev(24) / ((float) (1 << 24));
    }

    @Override
    public double prevDouble() {
        return (prev(27) + ((long) (prev(26)) << 27)) * DOUBLE_UNIT;
    }

    @Override
    public double prevGaussian() {
        //FIXME
        throw new UnsupportedOperationException();
    }
}
