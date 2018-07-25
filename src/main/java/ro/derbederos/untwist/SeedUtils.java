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

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import static java.lang.Integer.toUnsignedLong;

public class SeedUtils {

    public static long convertToLong(int... seed) {
        long result = 0;
        int endIndex = seed.length / 2 * 2;
        for (int i = 0; i < endIndex; i += 2) {
            long high = toUnsignedLong(seed[i]) << 32;
            long low = toUnsignedLong(seed[i + 1]);
            result = 4294967291L * result + high | low;
        }
        if (endIndex != seed.length) {
            long low = seed[seed.length - 1];
            result = 4294967291L * result + low;
        }
        return result;
    }

    public static int convertToInt(long input) {
        final int high = (int) (input >>> 32);
        final int low = (int) (input & 0xFFFFFFFFL);
        final int prime = 65521;
        return high * prime + low;
    }

    public static int convertToInt(int... seed) {
        // The following number is the largest prime that fits
        // in 16 bits (i.e. 2^32 - 5).
        final int prime = 65521;

        int combined = 0;
        for (int s : seed) {
            combined = combined * prime + s;
        }

        return combined;
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
