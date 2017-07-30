package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomGeneratorFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.SecureRandom;

public class RandomUtils {
    static long convertToLong(int... seed) {
        return RandomGeneratorFactory.convertToLong(seed);
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
