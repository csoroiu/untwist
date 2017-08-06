package ro.derbederos.untwist;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.SecureRandom;

public class RandomUtils {
    static long convertToLong(int... seed) {
        long result = 0;
        int endIndex = seed.length / 2 * 2;
        for (int i = 0; i < endIndex; i += 2) {
            long hi = (long) seed[i] << 32;
            long low = (long) seed[i + 1] & 0xFFFFFFFFL;
            result = 4294967291L * result + hi | low;
        }
        if (endIndex != seed.length) {
            long low = seed[seed.length - 1];
            result = 4294967291L * result + low;
        }
        return result;
    }

    static int convertToInt(int high, int low) {
        final int prime = 65521;
        return high * prime + low;
    }

    static int convertToInt(int... seed) {
        // The following number is the largest prime that fits
        // in 16 bits (i.e. 2^32 - 5).
        final int prime = 65521;

        int combined = 0;
        for (int s : seed) {
            combined = combined * prime + s;
        }

        return combined;
    }

    public static long generateSecureRandomLongSeed() {
        byte[] bytes = SecureRandom.getSeed(Long.BYTES);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();
    }

    public static int[] generateSecureRandomIntArraySeed(int size) {
        byte[] bytes = SecureRandom.getSeed(Integer.BYTES * size);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        IntBuffer intbuffer = buffer.asIntBuffer();
        int[] result = new int[size];
        intbuffer.get(result);
        return result;
    }
}
