package ro.sixi.eval.random;

import org.apache.commons.math3.random.RandomGenerator;

public interface ReverseRandomGenerator extends RandomGenerator {
    void prevBytes(byte[] bytes);

    int prevInt();

    int prevInt(int bound);

    long prevLong();

    boolean prevBoolean();

    float prevFloat();

    double prevDouble();

    double prevGaussian();
}
