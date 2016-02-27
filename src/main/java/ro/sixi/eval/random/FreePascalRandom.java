package ro.sixi.eval.random;

/**
 * FreePascal uses Mersenne Twister, but it must be initialized with a 32bit seed. Initialization with a 32 bit seed is
 * identical to freepascal one.
 * 
 * Also, another difference is the nextDouble function which is using only 32 random bits to get a number.
 */
public class FreePascalRandom extends MersenneTwisterPy3kCompat {
    // freepascal https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
    // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("deprecation")
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
    public double nextDouble() {
        return ((long) next(32) & 0xffffffffL) * 0x1.0p-32d;
    }
}
