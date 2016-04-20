package ro.sixi.eval.random;

import org.apache.commons.math3.random.MersenneTwisterTest;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MersenneTwisterPy3kRandomGeneratorTest extends MersenneTwisterTest {

    @Override
    protected RandomGenerator makeGenerator() {
        return new MersenneTwisterPy3kCompat(1234567890123L);
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        try {
            generator.nextInt(-1);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
        }
        try {
            generator.nextInt(0);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNeg() {
        generator.nextInt(-1);
    }
}
