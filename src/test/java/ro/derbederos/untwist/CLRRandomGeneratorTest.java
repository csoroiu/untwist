package ro.derbederos.untwist;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static ro.derbederos.untwist.ArrayUtils.generateIntArray;
import static ro.derbederos.untwist.Utils.between;
import static ro.derbederos.untwist.Utils.createStream;
import static ro.derbederos.untwist.Utils.reverseArray;

public class CLRRandomGeneratorTest extends ReverseRandomGeneratorAbstractTest<CLRRandom> {

    @Override
    protected CLRRandom makeGenerator() {
        return new CLRRandom(new int[]{291, 564, 837, 1110});
    }

    @Override
    @Test
    public void testNextInt2() {
        //FIXME
        //super.testNextInt2();
    }

    @Override
    @Test
    public void testNextIntWideRange() {
        //FIXME
        //super.testNextIntWideRange();
    }

    @Test
    @Ignore
    public void testNextInt_NoBoundStream() {
        IntPredicate betweenPredicate = (i) -> 0 <= i && i < Integer.MAX_VALUE;
        boolean result = createStream(1_000_000_000L, () -> generator.nextInt()).allMatch(betweenPredicate);
        assertThat(result, equalTo(true));
    }

    @Test
    public void testNextInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(200, 100);
    }

    @Test
    public void testPrevInt_InvalidRange() {
        expectedException.expect(IllegalArgumentException.class);

        generator.prevInt(200, 100);
    }

    @Test
    public void testNextInt() {
        final Matcher<Integer> betweenMatcher = between(0, 1000);
        createStream(100000, () -> generator.nextInt(1000))
                .forEach((t) -> assertThat(t, betweenMatcher));
    }

    @Test
    public void testNextPrevIntRange() {
        int[] expected = generateIntArray(2459, () -> generator.nextInt(-1000, 3000));
        int[] actual = generateIntArray(2459, () -> generator.prevInt(-1000, 3000));

        IntStream.of(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.nextInt(-1000, 3000));
        actual = generateIntArray(2467, () -> generator.prevInt(-1000, 3000));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextIntRange() {
        int[] expected = generateIntArray(2459, () -> generator.prevInt(-1000, 3000));
        int[] actual = generateIntArray(2459, () -> generator.nextInt(-1000, 3000));

        IntStream.of(expected).forEach((t) -> assertThat(t, between(-1000, 3000)));
        assertThat(actual, equalTo(reverseArray(expected)));

        expected = generateIntArray(2467, () -> generator.prevInt(-1000, 3000));
        actual = generateIntArray(2467, () -> generator.nextInt(-1000, 3000));

        assertThat(actual, equalTo(reverseArray(expected)));
    }

    // -----------
    // STATE TESTS
    // -----------
    private static final int COMPARE_STEPS = 100_000;

    @Test
    public void testStateNextPrev() {
        int[] expected = generator.getState();

        //going forward
        generator.nextInt();
        //going back to the initial state
        generator.prevInt();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.nextInt()) {
            int[] expected = generator.getState();

            //going forward
            generator.nextInt();
            //going back to the initial state
            generator.prevInt();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNext() {
        int[] expected = generator.getState();

        //going back
        generator.prevInt();
        //going forward to the initial state
        generator.nextInt();

        int[] actual = generator.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;

        for (int i = 0; i < COMPARE_STEPS; i++, generator.prevInt()) {
            int[] expected = generator.getState();

            //going back
            generator.prevInt();
            //going forward to the initial state
            generator.nextInt();

            int[] actual = generator.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
                assertThat(errors, lessThan(10));
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextVsNextPrev() {
        CLRRandom clrRandom1 = new CLRRandom(new int[]{291, 564, 837, 1110});
        clrRandom1.prevInt();
        clrRandom1.nextInt();
        int[] expected = clrRandom1.getState();


        CLRRandom clrRandom2 = new CLRRandom(new int[]{291, 564, 837, 1110});
        clrRandom2.prevInt();
        clrRandom2.nextInt();
        int[] actual = clrRandom2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
