/*
 * Copyright (c) 2017 Claudiu Soroiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.derbederos.untwist;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class RandomUtils {
    public static IntStream nextInts(RandomGenerator generator) {
        return IntStream.generate(generator::nextInt);
    }

    public static IntStream prevInts(ReverseRandomGenerator generator) {
        return IntStream.generate(generator::prevInt);
    }

    public static IntStream nextInts(long streamSize, RandomGenerator generator) {
        return IntStream.generate(generator::nextInt).limit(streamSize);
    }

    public static IntStream prevInts(long streamSize, ReverseRandomGenerator generator) {
        return IntStream.generate(generator::prevInt).limit(streamSize);
    }

    public static IntStream nextInts(int origin, int bound, RandomGenerator generator) {
        checkRange(origin, bound);
        return IntStream.generate(() -> generator.nextInt(bound - origin) + origin);
    }

    public static IntStream prevInts(int origin, int bound, ReverseRandomGenerator generator) {
        checkRange(origin, bound);
        return IntStream.generate(() -> generator.prevInt(bound - origin) + origin);
    }

    public static IntStream nextInts(long streamSize, int origin, int bound, RandomGenerator generator) {
        checkRange(origin, bound);
        return IntStream.generate(() -> generator.nextInt(bound - origin) + origin).limit(streamSize);
    }

    public static IntStream prevInts(long streamSize, int origin, int bound, ReverseRandomGenerator generator) {
        checkRange(origin, bound);
        return IntStream.generate(() -> generator.prevInt(bound - origin) + origin).limit(streamSize);
    }

    public static LongStream nextLongs(RandomGenerator generator) {
        return LongStream.generate(generator::nextLong);
    }

    public static LongStream prevLongs(ReverseRandomGenerator generator) {
        return LongStream.generate(generator::prevLong);
    }

    public static LongStream nextLongs(long streamSize, RandomGenerator generator) {
        return LongStream.generate(generator::nextLong).limit(streamSize);
    }

    public static LongStream prevLongs(long streamSize, ReverseRandomGenerator generator) {
        return LongStream.generate(generator::prevLong).limit(streamSize);
    }

    public static LongStream nextLongs(long origin, long bound, BitsStreamGenerator generator) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.nextLong(bound - origin) + origin);
    }

    public static LongStream prevLongs(long origin, long bound, ReverseBitsStreamGenerator generator) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.prevLong(bound - origin) + origin);
    }

    public static LongStream nextLongs(long streamSize, long origin, long bound, BitsStreamGenerator generator) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.nextLong(bound - origin) + origin).limit(streamSize);
    }

    public static LongStream prevLongs(long streamSize, long origin, long bound, ReverseBitsStreamGenerator generator) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.prevLong(bound - origin) + origin).limit(streamSize);
    }

    public static DoubleStream nextDoubles(RandomGenerator generator) {
        return DoubleStream.generate(generator::nextDouble);
    }

    public static DoubleStream prevDoubles(ReverseRandomGenerator generator) {
        return DoubleStream.generate(generator::prevDouble);
    }

    public static DoubleStream nextDoubles(long streamSize, RandomGenerator generator) {
        return DoubleStream.generate(generator::nextDouble).limit(streamSize);
    }

    public static DoubleStream prevDoubles(long streamSize, ReverseRandomGenerator generator) {
        return DoubleStream.generate(generator::prevDouble).limit(streamSize);
    }

    private static void checkRange(int origin, int bound) {
        if (origin < bound && bound - origin <= 0) {
            throw new IllegalArgumentException("range not representable as int");
        }
    }

    private static void checkRange(long origin, long bound) {
        if (origin < bound && bound - origin <= 0) {
            throw new IllegalArgumentException("range not representable as long");
        }
    }

    public static int generateSecureRandomIntSeed() {
        byte[] bytes = SecureRandom.getSeed(Integer.BYTES);
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static long generateSecureRandomLongSeed() {
        byte[] bytes = SecureRandom.getSeed(Long.BYTES);
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static int[] generateSecureRandomIntArraySeed(int size) {
        byte[] bytes = SecureRandom.getSeed(Integer.BYTES * size);
        int[] result = new int[size];
        ByteBuffer.wrap(bytes).asIntBuffer().get(result);
        return result;
    }
}
