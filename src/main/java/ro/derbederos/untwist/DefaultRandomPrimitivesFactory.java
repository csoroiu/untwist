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

class DefaultRandomPrimitivesFactory {

    static boolean toBoolean(int n) {
        return n != 0;
    }

    static int nextInt(int bound, ReverseRandomGenerator generator) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be strictly positive");
        }

        int r = generator.nextInt() >>> 1;
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int) ((bound * (long) r) >> 31);
        else {
            for (int u = r;                       // ensure nonnegative
                 u - (r = u % bound) + m < 0;     // rejection check
                 u = generator.nextInt() >>> 1)   // retry
                ;
        }
        return r;
    }

    static int prevInt(int bound, ReverseRandomGenerator generator) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be strictly positive");
        }

        int r = generator.prevInt() >>> 1;
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int) ((bound * (long) r) >> 31);
        else {
            for (int u = r;                       // ensure nonnegative
                 u - (r = u % bound) + m < 0;     // rejection check
                 u = generator.prevInt() >>> 1)   // retry
                ;
        }
        return r;
    }

    static int nextInt(int origin, int bound, ReverseRandomGenerator generator) {
        if (origin < bound) {
            int n = bound - origin;
            if (n > 0) {
                return generator.nextInt(n) + origin;
            } else {  // range not representable as int
                int r;
                do {
                    r = generator.nextInt();
                } while (r < origin || r >= bound);
                return r;
            }
        } else {
            return generator.nextInt();
        }
    }

    static int prevInt(int origin, int bound, ReverseRandomGenerator generator) {
        if (origin < bound) {
            int n = bound - origin;
            if (n > 0) {
                return generator.prevInt(n) + origin;
            } else {  // range not representable as int
                int r;
                do {
                    r = generator.prevInt();
                } while (r < origin || r >= bound);
                return r;
            }
        } else {
            return generator.prevInt();
        }
    }

    static long nextLong(long bound, ReverseRandomGenerator generator) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be strictly positive");
        }

        long r = generator.nextLong();
        long m = bound - 1;
        if ((bound & m) == 0) {  // i.e., bound is a power of 2
            r = r & m;
        } else {
            for (long u = r >>> 1;                // ensure nonnegative
                 u + m - (r = u % bound) < 0L;    // rejection check
                 u = generator.nextLong() >>> 1)  // retry
                ;
        }
        return r;
    }

    static long prevLong(long bound, ReverseRandomGenerator generator) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be strictly positive");
        }

        long r = generator.prevLong();
        long m = bound - 1;
        if ((bound & m) == 0) {  // i.e., bound is a power of 2
            r = r & m;
        } else {
            for (long u = r >>> 1;                // ensure nonnegative
                 u + m - (r = u % bound) < 0L;    // rejection check
                 u = generator.prevLong() >>> 1)  // retry
                ;
        }
        return r;
    }
}
