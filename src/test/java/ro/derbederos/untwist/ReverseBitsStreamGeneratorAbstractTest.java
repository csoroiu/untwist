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

import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextLongs;
import static ro.derbederos.untwist.RandomUtils.prevLongs;
import static ro.derbederos.untwist.Utils.reverseArray;


public abstract class ReverseBitsStreamGeneratorAbstractTest<T extends ReverseBitsStreamGenerator>
        extends ReverseRandomGeneratorAbstractTest<T> {

    @Override
    protected abstract T makeGenerator();

    @Test
    public void testNextLongNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextLong(-16);
    }

    @Test
    public void testPrevLongNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.prevLong(-16);
    }

    @Test
    public void testNextPrevLongBound() {
        long[] expected = nextLongs(2459, 0, 0x7ABCDEL, generator).toArray();
        long[] actual = prevLongs(2459, 0, 0x7ABCDEL, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = nextLongs(2467, 0, 0x7ABCDEL, generator).toArray();
        actual = prevLongs(2467, 0, 0x7ABCDEL, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLongBound() {
        long[] expected = prevLongs(2459, 0, 0x7ABCDEF8FFFFFFFFL, generator).toArray();
        long[] actual = nextLongs(2459, 0, 0x7ABCDEF8FFFFFFFFL, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = prevLongs(2467, 0, 0x7ABCDEF8FFFFFFFFL, generator).toArray();
        actual = nextLongs(2467, 0, 0x7ABCDEF8FFFFFFFFL, generator).toArray();

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    @UseDataProvider("dataProviderTestNextPrevBytes")
    public void testNextBytesRange(int size) {
        byte BYTE0 = 0;
        byte[] actual = new byte[size];
        generator.nextBytes(actual, 2, size - 3);
        assertThat(actual[0], equalTo(BYTE0));
        assertThat(actual[1], equalTo(BYTE0));
        assertThat(actual[size - 1], equalTo(BYTE0));
    }

    @Test
    @UseDataProvider("dataProviderTestNextPrevBytes")
    public void testNextPrevBytesRange(int size) {
        byte[] expected1 = new byte[size];
        byte[] actual1 = new byte[size];

        generator.nextBytes(expected1, 2, size - 4);
        generator.prevBytes(actual1, 2, size - 4);
        assertThat(actual1, equalTo(reverseArray(expected1)));

        byte[] expected2 = new byte[size];
        byte[] actual2 = new byte[size];

        generator.nextBytes(expected2, 2, size - 4);
        generator.prevBytes(actual2, 2, size - 4);

        assertThat(actual2, equalTo(reverseArray(expected2)));
        assertThat(expected1, equalTo(expected2));
    }

    @Test
    @UseDataProvider("dataProviderTestNextPrevBytes")
    public void testPrevNextBytesRange(int size) {
        byte[] expected1 = new byte[size];
        byte[] actual1 = new byte[size];

        generator.prevBytes(expected1, 2, size - 4);
        generator.nextBytes(actual1, 2, size - 4);
        assertThat(actual1, equalTo(reverseArray(expected1)));

        byte[] expected2 = new byte[size];
        byte[] actual2 = new byte[size];

        generator.prevBytes(expected2, 2, size - 4);
        generator.nextBytes(actual2, 2, size - 4);

        assertThat(actual2, equalTo(reverseArray(expected2)));
        assertThat(expected1, equalTo(expected2));
    }

    @Test
    public abstract void testNextLong16ExactValue();
}
