package ro.sixi.eval.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.MersenneTwister;

public class MersenneTwisterPyCompat extends MersenneTwister {

    private static final long serialVersionUID = 1L;

    public MersenneTwisterPyCompat() {
        super();
    }

    public MersenneTwisterPyCompat(int seed) {
        this(new int[] { seed });
    }

    public MersenneTwisterPyCompat(int[] seed) {
        super(seed);
    }

    public MersenneTwisterPyCompat(long seed) {
        super(seed);
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed(new int[] { (int) seed });
        } else {
            setSeed(new int[] { (int) (seed & 0xffffffffl), high });
        }
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_random
    public double nextDouble() {
        return (((long) (next(27)) << 26) + next(26)) * 0x1.0p-53;
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override
    public int nextInt(int n) throws IllegalArgumentException {
        if (n > 0) {
            final int bit_length = Integer.SIZE - Integer.numberOfLeadingZeros(n);
            int bits;
            do {
                bits = next(bit_length);
            } while (bits >= n);
            return bits;
        }
        throw new NotStrictlyPositiveException(n);
    }

    @Override
    public long nextLong() {
        final long low = ((long) next(32)) & 0xffffffffL;
        final long high = ((long) next(32)) << 32;
        return high | low;
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_getrandbits
    public void nextBytes(byte[] bytes) {
        int i = 0;
        final int iEnd = bytes.length - 4;
        while (i < iEnd) {
            final int random = next(32);
            bytes[i] = (byte) (random & 0xff);
            bytes[i + 1] = (byte) ((random >> 8) & 0xff);
            bytes[i + 2] = (byte) ((random >> 16) & 0xff);
            bytes[i + 3] = (byte) ((random >> 24) & 0xff);
            i += 4;
        }
        int random = next(32);
        final int shift = 32 - (bytes.length - i) * 8;
        random >>>= shift;
        while (i < bytes.length) {
            bytes[i++] = (byte) (random & 0xff);
            random >>= 8;
        }
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_getrandbits
    public long nextLong(long n) throws IllegalArgumentException {
        if (n > 0) {
            final int bit_length = Long.SIZE - Long.numberOfLeadingZeros(n);
            long bits;
            do {
                bits = ((long) next(Math.min(32, bit_length))) & 0xffffffffL;
                if (bit_length > 32) {
                    bits = bits | ((long) next(bit_length - 32)) << 32;
                }
            } while (bits >= n);
            return bits;
        }
        throw new NotStrictlyPositiveException(n);
    }
}
