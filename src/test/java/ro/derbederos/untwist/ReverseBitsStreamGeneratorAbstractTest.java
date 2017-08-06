package ro.derbederos.untwist;

import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.generateLongArray;
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
        long[] expected = generateLongArray(2459, () -> generator.nextLong(0x7ABCDEL));
        long[] actual = generateLongArray(2459, () -> generator.prevLong(0x7ABCDEL));

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.nextLong(0x7ABCDEL));
        actual = generateLongArray(2467, () -> generator.prevLong(0x7ABCDEL));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextLongBound() {
        long[] expected = generateLongArray(2459, () -> generator.prevLong(0x7ABCDEF8FFFFFFFFL));
        long[] actual = generateLongArray(2459, () -> generator.nextLong(0x7ABCDEF8FFFFFFFFL));

        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateLongArray(2467, () -> generator.prevLong(0x7ABCDEF8FFFFFFFFL));
        actual = generateLongArray(2467, () -> generator.nextLong(0x7ABCDEF8FFFFFFFFL));

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
