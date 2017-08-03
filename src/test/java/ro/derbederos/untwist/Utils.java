package ro.derbederos.untwist;

import org.hamcrest.Matcher;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static ro.derbederos.untwist.ArrayUtils.generateArray;

@Ignore
class Utils {

    static List<Byte> toByteList(byte[] b) {
        return Arrays.asList(generateArray(Byte[]::new, b.length, (i) -> (b[i])));
    }

    static <T extends java.lang.Comparable<T>> Matcher<T> between(T minValue, T maxValue) {
        return allOf(greaterThanOrEqualTo(minValue), lessThan(maxValue));
    }

    static IntStream createStream(long limit, IntSupplier supplier) {
        return IntStream.generate(supplier).limit(limit);
    }

    static DoubleStream createStream(long limit, DoubleSupplier supplier) {
        return DoubleStream.generate(supplier).limit(limit);
    }

    static byte[] reverseArray(byte[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final byte tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }

    static int[] reverseArray(int[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final int tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }

    static long[] reverseArray(long[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final long tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }

    static double[] reverseArray(double[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final double tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }

    static float[] reverseArray(float[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final float tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }

    static boolean[] reverseArray(boolean[] b) {
        int j = b.length - 1;
        for (int i = 0; i < b.length / 2; i++) {
            final boolean tmp = b[i];
            b[i] = b[j];
            b[j--] = tmp;
        }
        return b;
    }
}
