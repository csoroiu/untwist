package ro.sixi.eval.util;

import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;

public class JDKRandomPy3kCompat extends Random implements RandomGenerator {

    private static final long serialVersionUID = 1L;

    public JDKRandomPy3kCompat() {
        super();
    }

    public JDKRandomPy3kCompat(long seed) {
        super(seed);
    }

    @Override
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomGeneratorFactory.convertToLong(seed));
    }

    @Override
    @Deprecated
    public float nextFloat() {
        throw new UnsupportedOperationException(
                "nextFloat - python supports only double precision floating point numbers");
    }
}
