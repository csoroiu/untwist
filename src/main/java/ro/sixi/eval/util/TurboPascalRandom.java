package ro.sixi.eval.util;

import org.apache.commons.math3.random.RandomGenerator;

/*
 * http://stackoverflow.com/questions/3946869/how-reliable-is-the-random-function-in-delphi#3953956
 * http://labs.icb.ufmg.br/lbcd/prodabi5/homepages/hugo/Hugo/TPB/RTL/SYS/RAND.ASM
 * https://github.com/rofl0r/KOL/blob/master/system/system.pas
 * http://people.cs.nctu.edu.tw/~tsaiwn/sisc/runtime_error_200_div_by_0/www.merlyn.demon.co.uk/#pas
 * http://forum.lazarus.freepascal.org/index.php?topic=2665.0
 * http://virtualschool.edu/mon/Crypto/RandomNumberMath
 * gnu pascal
 * http://latel.upf.edu/morgana/altres/pub/gpc/list2html/1997/mail0911.htm
 */
public class TurboPascalRandom implements RandomGenerator {
    long seed;

    public TurboPascalRandom(int seed) {
        setSeed(seed);
    }

    public TurboPascalRandom(long seed) {
        setSeed(seed);
    }

    private void nextSeed() {
        seed = (seed * 0x08088405L + 1) & 0xFFFFFFFFL;
    }

    private void prevSeed() {
        seed = ((seed - 1) * 0xD94FA8CDL) & 0xFFFFFFFFL;
    }

    private final boolean coprocEnabled = true;

    // RandSeed = -1498392781 precedes 0
    // http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt - _randExt
    @Override
    public double nextDouble() {
        nextSeed();
        if (coprocEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return (int) seed / (double) (1L << 32) + 0.5;
        } else {
            return seed / (double) (1L << 32);
        }
    }

    @Override
    public int nextInt(int n) {
        nextSeed();
        return (int) ((seed * n) >>> 32);
    }

    @Override
    public int nextInt() {
        nextSeed();
        return (int) seed;
    }

    @Override
    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(convertToInt(seed));
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        final int low = (int) (seed & 0xffffffffL);
        setSeed(convertToInt(high, low));
    }

    private static int convertToInt(int... seed) {
        // The following number is the largest prime that fits
        // in 16 bits (i.e. 2^32 - 5).
        final int prime = 65521;

        int combined = 0;
        for (int s : seed) {
            combined = combined * prime + s;
        }

        return combined;
    }

    @Override
    public long nextLong() {
        return ((long) (nextInt()) << 32) + nextInt();
    }

    @Override
    public boolean nextBoolean() {
        return nextInt(2) == 0;
    }

    @Override
    @Deprecated
    public float nextFloat() {
        throw new UnsupportedOperationException(
                "nextFloat - python supports only double precision floating point numbers");
    }

    @Override
    public void nextBytes(byte[] bytes) {
        // TODO Auto-generated method stub
    }

    @Override
    public double nextGaussian() {
        // TODO Auto-generated method stub
        return 0;
    }
}
