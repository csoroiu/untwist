package ro.derbederos.untwist;

public class MersenneTwisterPy3kRandomGeneratorTest extends ReversibleMersenneTwisterTest {

    @Override
    protected ReversibleMersenneTwister makeGenerator() {
        return new MersenneTwisterPy3k(123456789013L);
    }

    @Override
    int[] getMakotoNishimuraTestSeed() {
        return new int[]{0x456, 0x345, 0x234, 0x123};
    }
}
