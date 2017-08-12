package ro.derbederos.untwist;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ReversibleJdkRandom extends Random implements ReverseRandomGenerator {
    private static final long serialVersionUID = 1L;

    private final static long multiplier = 0x5DEECE66DL;
    private final static long invmultiplier = 0xDFE05BCB1365L;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;
    private final static double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    private final AtomicLong seedRef = getReferenceToSeed();

    private static final Field SEED_FIELD;

    static {
        Field seedField = null;
        try {
            final PrivilegedExceptionAction<Field> action =
                    () -> {
                        final Field f = Random.class.getDeclaredField("seed");
                        f.setAccessible(true);
                        return f;
                    };

            seedField = AccessController.doPrivileged(action);
        } catch (final Exception ex) {
            rethrowUnchecked(ex);
        }
        SEED_FIELD = seedField;
    }

    private static void rethrowUnchecked(final Throwable ex) {
        ReversibleJdkRandom.<RuntimeException>rethrow(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void rethrow(final Throwable t) throws T {
        throw (T) t;
    }

    public ReversibleJdkRandom() {
        super();
    }

    public ReversibleJdkRandom(long seed) {
        super(seed);
    }

    private AtomicLong getReferenceToSeed() {
        try {
            return (AtomicLong) SEED_FIELD.get(this);
        } catch (IllegalAccessException ignore) {
        }
        return null;
    }

    @Override
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToLong(seed));
    }

    @Override
    public synchronized void setSeed(long seed) {
        clear();
        super.setSeed(seed);
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

    /**
     * Works as a complete reverse of nextBytes. It will drop the first bits of the prevInt, and not the last.
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

    @Override
    public int prevInt() {
        return prev(32);
    }

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

    private void clear() {
        shouldReverseGaussian = false;
        if (hasNextGaussian) {
            super.nextGaussian();
            hasNextGaussian = false;
        }
    }

    private boolean shouldReverseGaussian;
    private boolean hasNextGaussian;

    @Override
    public double nextGaussian() {
        shouldReverseGaussian = !shouldReverseGaussian;
        hasNextGaussian = !hasNextGaussian;
        return super.nextGaussian();
    }

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
