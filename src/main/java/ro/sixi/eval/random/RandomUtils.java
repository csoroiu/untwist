package ro.sixi.eval.random;

import org.apache.commons.math3.random.RandomGeneratorFactory;

class RandomUtils {
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
}
