package ro.sixi.eval.common;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

//https://github.com/votadlos/JavaCG/blob/master/JavaCG/JavaCG/JavaLCGMimic.cpp#L15
public class ReversibleRandom extends Random {
    private static final long serialVersionUID = 5566331316770172904L;

    private final static long multiplier = 0x5DEECE66DL;
    private final static long invmultiplier = 0xDFE05BCB1365L;
    private final static long addend = 0xBL;
    private final static long mask = (1L << 48) - 1;

    private AtomicLong seedRef;

    public ReversibleRandom(long seed) {
        super(seed);
        getReferenceToSeed();
    }

    private void getReferenceToSeed() {
        try {
            Field f = Random.class.getDeclaredField("seed");
            f.setAccessible(true);
            seedRef = (AtomicLong) f.get(this);
        } catch (Exception e) {
        }
    }

    public ReversibleRandom() {
        super();
        getReferenceToSeed();
    }

    protected int prev(int bits) {
        long nextSeed, prevseed;
        AtomicLong seed = this.seedRef;
        do {
            nextSeed = seed.get();
            prevseed = (invmultiplier * (nextSeed - addend)) & mask;
        } while (!seed.compareAndSet(nextSeed, prevseed));
        return (int) (prevseed >>> (48 - bits));
    }

    public int prevInt(int bound) {
        if (bound <= 0)
            return bound;

        if ((bound & -bound) == bound) // limit is power of 2
            return bound * prev(31) >> 31; // output limit*(seed >> 17) >> 31

        int j, k;
        do {// limit is not power of 2
            j = prevInt(31);// (seed >> 17)
            k = j % bound; // output is(seed >> 17) modulo limit
        } while ((j - k) + (bound - 1) < 0); // remove statistical bias
        return k;
    }

    public int prevInt() {
        return prev(32);
    }

    public boolean prevBoolean() {
        return prev(1) != 0;
    }
}
