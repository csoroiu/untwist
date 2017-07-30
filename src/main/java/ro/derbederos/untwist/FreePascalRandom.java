package ro.derbederos.untwist;

/**
 * FreePascal uses Mersenne Twister, but it must be initialized with a 32bit seed. Initialization with a 32 bit seed is
 * identical to freepascal one.
 * <p>
 * Also, another difference is the nextDouble function which is using only 32 random bits to get a number.
 */
public class FreePascalRandom extends ReversibleMersenneTwister {
    // freepascal https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
    // http://svn.freepascal.org/svn/fpc/trunk/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

    private static final long serialVersionUID = 1L;

    public FreePascalRandom() {
        super();
    }

    public FreePascalRandom(int seed) {
        this(new int[]{seed});
    }

    public FreePascalRandom(int[] seed) {
        super(seed);
    }

    public FreePascalRandom(long seed) {
        super(seed);
    }

    @SuppressWarnings("deprecation")
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
            setSeed(new int[]{high, (int) (seed & 0xffffffffL)});
        }
    }

    @Override
    public void setSeed(int[] seed) {
        if (seed.length == 1) {
            setSeed(seed[0]);
        } else {
            super.setSeed(reversedArray(seed));
        }
    }

    @Override
    public double nextDouble() {
        return ((long) next(32) & 0xffffffffL) * 0x1.0p-32d;
    }

    @Override
    public double prevDouble() {
        return ((long) prev(32) & 0xffffffffL) * 0x1.0p-32d;
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

    @Override
    // https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L668
    public int nextInt(int n) throws IllegalArgumentException {
        if (n < 0) {
            n++;
        }
        long urand = ((long) next(32)) & 0xffffffffL;
        return (int) ((urand * n) >>> 32);
    }

    @Override
    public int prevInt(int n) throws IllegalArgumentException {
        if (n < 0) {
            n++;
        }
        long urand = ((long) prev(32)) & 0xffffffffL;
        return (int) ((urand * n) >>> 32);
    }

    @Override
    public long nextLong() {
        final long low = ((long) next(32)) & 0xffffffffL;
        final long high = ((long) next(32)) << 32;
        return high | low;
    }

    @Override
    public long prevLong() {
        final long high = ((long) prev(32)) << 32;
        final long low = ((long) prev(32)) & 0xffffffffL;
        return high | low;
    }

    @Override
    // https://github.com/graemeg/freepascal/blob/5186987/rtl/inc/system.inc#L676
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

    @Override
    public long prevLong(long n) throws IllegalArgumentException {
        long high = ((long) prev(32)) & 0x7fffffffL; // drop highest one bit
        long low = ((long) prev(32)) & 0xffffffffL;
        long value = low | (high << 32);
        if (n != 0) {
            return value % n;
        } else {
            return 0;
        }
    }

    private static int[] reversedArray(int[] seed) {
        int[] seedReversed = new int[seed.length];
        int j = seed.length;
        for (int i = 0; i < seedReversed.length; i++) {
            seedReversed[i] = seed[--j];
        }
        return seedReversed;
    }
}
