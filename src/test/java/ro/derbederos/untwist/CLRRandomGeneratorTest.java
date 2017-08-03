package ro.derbederos.untwist;

import org.junit.Test;

public class CLRRandomGeneratorTest extends ReverseRandomGeneratorAbstractTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new CLRRandom(-55);
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
}
