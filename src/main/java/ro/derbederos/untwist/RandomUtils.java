/*
 * Copyright (c) 2017-2018 Claudiu Soroiu
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

import org.apache.commons.rng.UniformRandomProvider;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class RandomUtils {
    public static IntStream nextInts(UniformRandomProvider generator) {
        return IntStream.generate(generator::nextInt);
    }

    public static IntStream prevInts(ReverseUniformRandomProvider generator) {
        return IntStream.generate(generator::prevInt);
    }

    public static IntStream nextInts(UniformRandomProvider generator, long streamSize) {
        return nextInts(generator).limit(streamSize);
    }

    public static IntStream prevInts(ReverseUniformRandomProvider generator, long streamSize) {
        return prevInts(generator).limit(streamSize);
    }

    public static IntStream nextInts(ReverseUniformRandomProvider generator, int origin, int bound) {
        return IntStream.generate(() -> generator.nextInt(origin, bound));
    }

    public static IntStream prevInts(ReverseUniformRandomProvider generator, int origin, int bound) {
        return IntStream.generate(() -> generator.prevInt(origin, bound));
    }

    public static IntStream nextInts(ReverseUniformRandomProvider generator, long streamSize, int origin, int bound) {
        return nextInts(generator, origin, bound).limit(streamSize);
    }

    public static IntStream prevInts(ReverseUniformRandomProvider generator, long streamSize, int origin, int bound) {
        return prevInts(generator, origin, bound).limit(streamSize);
    }

    public static LongStream nextLongs(UniformRandomProvider generator) {
        return LongStream.generate(generator::nextLong);
    }

    public static LongStream prevLongs(ReverseUniformRandomProvider generator) {
        return LongStream.generate(generator::prevLong);
    }

    public static LongStream nextLongs(UniformRandomProvider generator, long streamSize) {
        return nextLongs(generator).limit(streamSize);
    }

    public static LongStream prevLongs(ReverseUniformRandomProvider generator, long streamSize) {
        return prevLongs(generator).limit(streamSize);
    }

    public static LongStream nextLongs(ReverseUniformRandomProvider generator, long origin, long bound) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.nextLong(bound - origin) + origin);
    }

    public static LongStream prevLongs(ReverseUniformRandomProvider generator, long origin, long bound) {
        checkRange(origin, bound);
        return LongStream.generate(() -> generator.prevLong(bound - origin) + origin);
    }

    public static LongStream nextLongs(ReverseUniformRandomProvider generator, long streamSize, long origin, long bound) {
        return nextLongs(generator, origin, bound).limit(streamSize);
    }

    public static LongStream prevLongs(ReverseUniformRandomProvider generator, long streamSize, long origin, long bound) {
        return prevLongs(generator, origin, bound).limit(streamSize);
    }

    public static DoubleStream nextDoubles(UniformRandomProvider generator) {
        return DoubleStream.generate(generator::nextDouble);
    }

    public static DoubleStream prevDoubles(ReverseUniformRandomProvider generator) {
        return DoubleStream.generate(generator::prevDouble);
    }

    public static DoubleStream nextDoubles(UniformRandomProvider generator, long streamSize) {
        return nextDoubles(generator).limit(streamSize);
    }

    public static DoubleStream prevDoubles(ReverseUniformRandomProvider generator, long streamSize) {
        return prevDoubles(generator).limit(streamSize);
    }

    private static void checkRange(long origin, long bound) {
        if (origin < bound && bound - origin <= 0) {
            throw new IllegalArgumentException("range not representable as long");
        }
    }
}
