package ro.sixi.eval.random;

import org.apache.commons.math3.random.MersenneTwister;

/**
 * FreePascal uses Mersenne Twister, but it must be initialized with a 32bit seed. Initialization with a 32 bit seed is
 * identical to freepascal one.
 * 
 * Also, another difference is the nextDouble function which is using only 32 random bits to get a number.
 */
public class FreePascalRandom extends MersenneTwister {
    // freepascal https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
    // http://svn.freepascal.org/svn/fpc/trunk/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

    private static final long serialVersionUID = 1L;

    public FreePascalRandom(long l) {
        super(l);
    }

    public FreePascalRandom(int l) {
        super(l);
    }

    public FreePascalRandom(int[] l) {
        super(l);
    }

    public FreePascalRandom() {
        super();
    }

    @Override
    public void setSeed(int seed) {
        super.setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed((int) seed);
        } else {
            setSeed(new int[] { (int) (seed & 0xffffffffL), high });
        }
    }

    @Override
    public void setSeed(int[] seed) {
        if (seed.length == 1) {
            setSeed(seed[0]);
        } else {
            super.setSeed(seed);
        }
    }

    @Override
    public double nextDouble() {
        return ((long) next(32) & 0xffffffffL) * 0x1.0p-32d;
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc#L632
    public int nextInt(int n) throws IllegalArgumentException {
        if (n < 0) {
            n++;
        }
        long urand = ((long) next(32)) & 0xffffffffL;
        return (int) ((urand * n) >>> 32);
    }

    @Override
    public long nextLong() {
        final long low = ((long) next(32)) & 0xffffffffL;
        final long high = ((long) next(32)) << 32;
        return high | low;
    }

    @Override
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc#L640
    public long nextLong(long n) throws IllegalArgumentException {
        long low = ((long) next(32)) & 0xffffffffL;
        long high = ((long) next(32)) & 0x7fffffffL; // drop highest one bit
        long value = low | (high << 32);
        if (n != 0) {
            return value % n;
        } else {
            return 0;
        }
    }
}
