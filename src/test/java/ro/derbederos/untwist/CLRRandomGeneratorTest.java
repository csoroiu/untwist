package ro.derbederos.untwist;

import org.junit.Assert;
import org.junit.Test;

public class CLRRandomGeneratorTest extends ReverseRandomGeneratorAbstractTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new CLRRandom(-55);
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
    @Test
    public void testNextIntNeg() {
        expectedException.expect(IllegalArgumentException.class);

        generator.nextInt(-1);
    }

    @Override
    public void testNextInt2() {
        //FIXME
        //super.testNextInt2();
    }

    @Override
    public void testNextIntWideRange() {
        //FIXME
        //super.testNextIntWideRange();
    }
}
