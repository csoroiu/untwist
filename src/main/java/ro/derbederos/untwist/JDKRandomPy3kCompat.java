package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

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
        setSeed(RandomUtils.convertToLong(seed));
    }

    @Override
    @Deprecated
    public float nextFloat() {
        return (float) nextDouble();
    }
}
