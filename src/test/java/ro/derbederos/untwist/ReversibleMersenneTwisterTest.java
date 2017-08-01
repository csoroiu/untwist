package ro.derbederos.untwist;

import org.apache.commons.math3.random.MersenneTwisterTest;
import org.apache.commons.math3.random.RandomGenerator;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.ArrayUtils.generateIntArray;
import static ro.derbederos.untwist.Utils.reverseArray;

public class ReversibleMersenneTwisterTest extends MersenneTwisterTest {

    private static final int COMPARE_STEPS = 10_000;
    private static final int[] ARRAY_SEED = {291, 564, 837, 1110};
    private static final long LONG_SEED = 123456789013L;
    private static final int INT_SEED = 1234557890;

    public ReversibleMersenneTwisterTest() {
    }

    protected RandomGenerator makeGenerator() {
        return new ReversibleMersenneTwister(LONG_SEED);
    }

    @Test
    public void testNextPrev() {
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);
        int[] expected = reverseArray(generateIntArray(10000, () -> mt.next(32)));
        int[] actual = generateIntArray(10000, () -> mt.prev(32));

        assertThat(actual, equalTo(expected));

        expected = generateIntArray(10000, () -> mt.next(32));
        actual = generateIntArray(10000, () -> mt.prev(32));

        MatcherAssert.assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testPrevNextBits() {
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);
        int[] expected = generateIntArray(626, () -> mt.prev(32));
        int[] actual = generateIntArray(626, () -> mt.next(32));

        MatcherAssert.assertThat(actual, equalTo(reverseArray(expected)));
    }

    @Test
    public void testStateNextPrev() {
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);
        int[] expected = mt.getState();

        //going forward
        mt.twist();
        //going back to the initial state
        mt.untwist();

        int[] actual = mt.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStateNextPrevMany() {
        int errors = 0;
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);

        for (int i = 0; i < COMPARE_STEPS; i++, mt.twist()) {
            int[] expected = mt.getState();

            //going forward
            mt.twist();
            //going back to the initial state
            mt.untwist();

            int[] actual = mt.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStateNextPrevManyIntSeed() {
        int errors = 0;
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(INT_SEED);

        for (int i = 0; i < COMPARE_STEPS; i++, mt.twist()) {
            int[] expected = mt.getState();

            //going forward
            mt.twist();
            //going back to the initial state
            mt.untwist();

            int[] actual = mt.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNext() {
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);
        int[] expected = mt.getState();

        //going back
        mt.untwist();
        //going forward to the initial state
        mt.twist();

        int[] actual = mt.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testStatePrevNextMany() {
        int errors = 0;
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(ARRAY_SEED);

        for (int i = 0; i < COMPARE_STEPS; i++, mt.untwist()) {
            int[] expected = mt.getState();

            //going back
            mt.untwist();
            //going forward to the initial state
            mt.twist();

            int[] actual = mt.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextManyIntSeed() {
        int errors = 0;
        ReversibleMersenneTwister mt = new ReversibleMersenneTwister(INT_SEED);

        for (int i = 0; i < COMPARE_STEPS; i++, mt.untwist()) {
            int[] expected = mt.getState();

            //going back
            mt.untwist();
            //going forward to the initial state
            mt.twist();

            int[] actual = mt.getState();

            //compare states
            try {
                assertThat(actual, equalTo(expected));
            } catch (AssertionError e) {
                System.err.println("Step " + i);
                e.printStackTrace();
                errors++;
            }
        }
        assertThat(errors, equalTo(0));
    }

    @Test
    public void testStatePrevNextVsNextPrev() {
        ReversibleMersenneTwister mt1 = new ReversibleMersenneTwister(ARRAY_SEED);
        mt1.untwist();
        mt1.twist();
        int[] expected = mt1.getState();


        ReversibleMersenneTwister mt2 = new ReversibleMersenneTwister(ARRAY_SEED);
        mt2.twist();
        mt2.untwist();
        int[] actual = mt2.getState();

        //compare states
        assertThat(actual, equalTo(expected));
    }
}
