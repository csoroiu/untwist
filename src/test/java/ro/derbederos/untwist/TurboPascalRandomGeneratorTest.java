package ro.derbederos.untwist;

import org.junit.Assert;
import org.junit.Test;

public class TurboPascalRandomGeneratorTest extends ReverseRandomGeneratorAbstractTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new TurboPascalRandom(0xC44002DC);
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
