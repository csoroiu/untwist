package ro.sixi.eval.random;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import lombok.experimental.UtilityClass;

import org.hamcrest.Matcher;
import org.junit.Ignore;

import ro.sixi.eval.util.ArrayUtils;

@UtilityClass
@Ignore
public class Utils {

    public static List<Byte> toByteList(byte[] b) {
        return Arrays.asList(ArrayUtils.generateArray(Byte[]::new, b.length, (i) -> Byte.valueOf(b[i])));
    }

    public static IntPredicate betweenPredicate(int minValue, int maxValue) {
        return (i) -> minValue <= i && i < maxValue;
    }

    public static <T extends java.lang.Comparable<T>> Matcher<T> between(T minValue, T maxValue) {
        return allOf(greaterThanOrEqualTo(minValue), lessThan(maxValue));
    }

    public static IntStream createStream(long limit, IntSupplier supplier) {
        return IntStream.generate(supplier).limit(limit);
    }

    public static DoubleStream createStream(long limit, DoubleSupplier supplier) {
        return DoubleStream.generate(supplier).limit(limit);
    }

}
