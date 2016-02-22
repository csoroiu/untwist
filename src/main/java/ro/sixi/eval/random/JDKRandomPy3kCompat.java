package ro.sixi.eval.random;

import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;

public class JDKRandomPy3kCompat extends Random implements RandomGenerator {

    private static final long serialVersionUID = 1L;

    public JDKRandomPy3kCompat() {
        super();
    }

    public JDKRandomPy3kCompat(long seed) {
        super(seed);
    }

    @Override
    @Deprecated
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToLong(seed));
    }

    @Override
    @Deprecated
    public float nextFloat() {
        throw new UnsupportedOperationException(
                "nextFloat - python supports only double precision floating point numbers");
    }
}
