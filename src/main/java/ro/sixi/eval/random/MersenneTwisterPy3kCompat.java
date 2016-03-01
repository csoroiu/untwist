package ro.sixi.eval.random;

import org.apache.commons.math3.random.MersenneTwister;

public class MersenneTwisterPy3kCompat extends MersenneTwister {

    private static final long serialVersionUID = 1L;

    public MersenneTwisterPy3kCompat() {
        super();
    }

    public MersenneTwisterPy3kCompat(int seed) {
        this(new int[] { seed });
    }

    public MersenneTwisterPy3kCompat(int[] seed) {
        super(seed);
    }

    public MersenneTwisterPy3kCompat(long seed) {
        super(seed);
    }

    @Override
    @Deprecated
    // Do not use this method directly
    public void setSeed(int seed) {
        super.setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed(new int[] { (int) seed });
        } else {
            setSeed(new int[] { high, (int) (seed & 0xffffffffL) });
        }
    }

    @Override
    public void setSeed(int[] seed) {
        // for python compatibility where the seed is a number (big integer)
        // and it is big endian
        int[] seedSwapped = new int[seed.length];
        int j = seed.length;
        for (int i = 0; i < seedSwapped.length; i++) {
            seedSwapped[i] = seed[--j];
        }
        super.setSeed(seedSwapped);
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_random
    public double nextDouble() {
        return (((long) (next(27)) << 26) + next(26)) * 0x1.0p-53;
    }

    @Override
    @Deprecated
    public float nextFloat() {
        throw new UnsupportedOperationException(
                "nextFloat - python supports only double precision floating point numbers");
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
        throw new IllegalArgumentException("n must be strictly positive");
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
        throw new IllegalArgumentException("n must be strictly positive");
    }
}
