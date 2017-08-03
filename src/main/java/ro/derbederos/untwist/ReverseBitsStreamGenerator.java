package ro.derbederos.untwist;

import org.apache.commons.math3.random.BitsStreamGenerator;

public abstract class ReverseBitsStreamGenerator extends BitsStreamGenerator implements ReverseRandomGenerator {
    @Override
    protected abstract int next(int bits);

    protected abstract int prev(int bits);

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
            return (int) (((long) bound * prev(31)) >>> 31);
        }

        int bits, val;
        do {
            bits = prev(31);
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return val;
    }

    @Override
    public long prevLong() {
        long low = ((long) prev(32)) & 0xffffffffL;
        long high = ((long) prev(32)) << 32;
        return low | high;
    }

    public long prevLong(long bound) throws IllegalArgumentException {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        long bits, val;
        do {
            bits = ((long) prev(32)) & 0xffffffffL;
            bits |= ((long) prev(31)) << 32;
            val = bits % bound;
        } while ((bits - val) + (bound - 1) < 0);
        return val;
    }

    @Override
    public boolean prevBoolean() {
        return prev(1) != 0;
    }

    @Override
    public float prevFloat() {
        return prev(23) * 0x1.0p-23f;

    }

    @Override
    public double prevDouble() {
        final int low = prev(26);
        final long high = ((long) prev(26)) << 26;
        return (low | high) * 0x1.0p-52d;
    }


    @Override
    public double prevGaussian() {
        //FIXME
        throw new UnsupportedOperationException();
    }
}
