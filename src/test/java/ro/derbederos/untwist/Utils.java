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

import org.hamcrest.Matcher;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

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
