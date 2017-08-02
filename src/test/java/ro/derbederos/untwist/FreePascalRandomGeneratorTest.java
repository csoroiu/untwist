package ro.derbederos.untwist;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FreePascalRandomGeneratorTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new FreePascalRandom(123456789013L);
    }

    @Override
    @Test
    public void testNextIntIAE2() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));

        nextInt = generator.nextInt(0);
        assertThat(nextInt, equalTo(0));
    }

    @Override
    @Test
    public void testNextIntNeg() {
        int nextInt = generator.nextInt(-1);
        assertThat(nextInt, equalTo(0));
    }
}
