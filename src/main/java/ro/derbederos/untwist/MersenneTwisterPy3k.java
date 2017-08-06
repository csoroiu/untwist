package ro.derbederos.untwist;

import org.apache.commons.math3.exception.OutOfRangeException;

public class MersenneTwisterPy3k extends ReversibleMersenneTwister {

    private static final long serialVersionUID = 1L;

    public MersenneTwisterPy3k() {
        super();
    }

    public MersenneTwisterPy3k(int seed) {
        this(new int[]{seed});
    }

    public MersenneTwisterPy3k(int[] seed) {
        super(seed);
    }

    public MersenneTwisterPy3k(long seed) {
        super(seed);
    }

    @Override
    public void setSeed(int seed) {
        super.setSeed(new int[]{seed});
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        if (high == 0) {
            setSeed(new int[]{(int) seed});
        } else {
            setSeed(new int[]{high, (int) (seed & 0xFFFFFFFFL)});
        }
    }

    @Override
    public void setSeed(int[] seed) {
        // for python compatibility where the seed is a number (big integer)
        // and it is big endian
        super.setSeed(reverseArray(seed));
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_random
    public double nextDouble() {
        return (((long) (next(27)) << 26) + next(26)) * 0x1.0p-53;
    }

    @Override
    public double prevDouble() {
        return (prev(26) + ((long) (prev(27)) << 26)) * 0x1.0p-53;
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
    public int prevInt(int n) throws IllegalArgumentException {
        if (n > 0) {
            final int bit_length = Integer.SIZE - Integer.numberOfLeadingZeros(n);
            int bits;
            do {
                bits = prev(bit_length);
            } while (bits >= n);
            return bits;
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    @Override
    public long nextLong() {
        final long low = ((long) next(32)) & 0xFFFFFFFFL;
        final long high = ((long) next(32)) << 32;
        return high | low;
    }

    @Override
    public long prevLong() {
        final long high = ((long) prev(32)) << 32;
        final long low = ((long) prev(32)) & 0xFFFFFFFFL;
        return high | low;
    }

    @Override
    public void nextBytes(byte[] bytes, int start, int len) {
        if (start < 0 || start >= bytes.length) {
            throw new OutOfRangeException(start, 0, bytes.length);
        }
        if (len < 0 || len > bytes.length - start) {
            throw new OutOfRangeException(len, 0, bytes.length - start);
        }

        nextBytesFill(bytes, start, len);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        nextBytesFill(bytes, 0, bytes.length);
    }

    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_getrandbits
    private void nextBytesFill(byte[] bytes, int start, int len) {
        int i = start;
        final int endIndex = start + len;
        final int iEnd = endIndex - 4;
        while (i < iEnd) {
            final int random = next(32);
            bytes[i] = (byte) (random & 0xFF);
            bytes[i + 1] = (byte) ((random >>> 8) & 0xFF);
            bytes[i + 2] = (byte) ((random >>> 16) & 0xFF);
            bytes[i + 3] = (byte) ((random >>> 24) & 0xFF);
            i += 4;
        }
        int random = next(32);
        final int k = (endIndex - i) * 8;
        random >>>= 32 - k;
        while (i < endIndex) {
            bytes[i++] = (byte) (random & 0xFF);
            random >>= 8;
        }
    }

    @Override
    public void prevBytes(byte[] bytes) {
        prevBytesFill(bytes, 0, bytes.length);
    }

    @Override
    public void prevBytes(byte[] bytes, int start, int len) {
        if (start < 0 ||
                start >= bytes.length) {
            throw new OutOfRangeException(start, 0, bytes.length);
        }
        if (len < 0 ||
                len > bytes.length - start) {
            throw new OutOfRangeException(len, 0, bytes.length - start);
        }

        prevBytesFill(bytes, start, len);
    }

    private void prevBytesFill(byte[] bytes, int start, int len) {
        int i = start;
        int iEnd = len - (len & 0x7ffffffc);
        if (iEnd != 0) {
            int random = prev(32);
            int indexLoopLimit = start + iEnd;
            while (i < indexLoopLimit) {
                bytes[i++] = (byte) ((random >> 24) & 0xFF);
                random <<= 8;
            }
        }
        int endIndex = start + len;
        while (i < endIndex) {
            final int random = prev(32);
            bytes[i] = (byte) ((random >>> 24) & 0xFF);
            bytes[i + 1] = (byte) ((random >>> 16) & 0xFF);
            bytes[i + 2] = (byte) ((random >>> 8) & 0xFF);
            bytes[i + 3] = (byte) (random & 0xFF);
            i += 4;
        }
    }

    @Override
    // http://svn.python.org/projects/python/trunk/Modules/_randommodule.c # random_getrandbits
    public long nextLong(long n) throws IllegalArgumentException {
        if (n > 0) {
            final int bit_length = Long.SIZE - Long.numberOfLeadingZeros(n);
            long bits;
            do {
                bits = ((long) next(Math.min(32, bit_length))) & 0xFFFFFFFFL;
                if (bit_length > 32) {
                    bits = bits | ((long) next(bit_length - 32)) << 32;
                }
            } while (bits >= n);
            return bits;
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    @Override
    public long prevLong(long n) throws IllegalArgumentException {
        if (n > 0) {
            final int bit_length = Long.SIZE - Long.numberOfLeadingZeros(n);
            long bits;
            do {
                bits = 0;
                if (bit_length > 32) {
                    bits = ((long) prev(bit_length - 32)) << 32;
                }
                bits |= ((long) prev(Math.min(32, bit_length))) & 0xFFFFFFFFL;
            } while (bits >= n);
            return bits;
        }
        throw new IllegalArgumentException("n must be strictly positive");

    }

    private static int[] reverseArray(int[] seed) {
        int[] seedReversed = new int[seed.length];
        int j = seed.length;
        for (int i = 0; i < seedReversed.length; i++) {
            seedReversed[i] = seed[--j];
        }
        return seedReversed;
    }
}
