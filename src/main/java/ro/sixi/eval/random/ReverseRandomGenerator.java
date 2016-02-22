package ro.sixi.eval.random;

public interface ReverseRandomGenerator {
    void prevBytes(byte[] bytes);

    void prevBytesMirror(byte[] bytes);

    int prevInt();

    int prevInt(int bound);

    long prevLong();

    boolean prevBoolean();

    float prevFloat();

    double prevDouble();

    double prevGaussian();
}
