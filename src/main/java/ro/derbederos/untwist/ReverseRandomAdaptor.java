package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

public class ReverseRandomAdaptor extends Random implements RandomGenerator {
    private static final long serialVersionUID = 1L;
    private final ReverseRandomGenerator randomGenerator;

    private ReverseRandomAdaptor() {
        this(null);
    }

    public ReverseRandomAdaptor(ReverseRandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public void setSeed(int seed) {
        randomGenerator.setSeed(seed);
    }

    @Override
    public void setSeed(int[] seed) {
        randomGenerator.setSeed(seed);
    }

    @Override
    public synchronized void setSeed(long seed) {
        randomGenerator.setSeed(seed);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        randomGenerator.prevBytes(bytes);
    }

    @Override
    public int nextInt() {
        return randomGenerator.prevInt();
    }

    @Override
    public int nextInt(int bound) {
        return randomGenerator.prevInt(bound);
    }

    @Override
    public long nextLong() {
        return randomGenerator.prevLong();
    }

    @Override
    public boolean nextBoolean() {
        return randomGenerator.prevBoolean();
    }

    @Override
    public float nextFloat() {
        return randomGenerator.prevFloat();
    }

    @Override
    public double nextDouble() {
        return randomGenerator.prevDouble();
    }

    @Override
    public synchronized double nextGaussian() {
        return randomGenerator.prevGaussian();
    }
}
