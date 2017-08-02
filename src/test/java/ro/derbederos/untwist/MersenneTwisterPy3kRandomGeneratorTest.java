package ro.derbederos.untwist;

public class MersenneTwisterPy3kRandomGeneratorTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new MersenneTwisterPy3k(123456789013L);
    }
}
