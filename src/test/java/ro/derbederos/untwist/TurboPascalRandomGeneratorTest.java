package ro.derbederos.untwist;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TurboPascalRandomGeneratorTest extends ReverseBitsStreamGeneratorAbstractTest<TurboPascalRandom> {

    @Override
    protected TurboPascalRandom makeGenerator() {
        return new TurboPascalRandom(0xC44002DC);
    }

    @Test
    public void testZero() {
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, true);
        assertThat(generator.nextDouble(), equalTo(0.0));
    }

    @Test
    public void testZero_CoprocDisabled() {
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, false);
        assertThat(generator.nextDouble(), equalTo(0.5));
    }

}
