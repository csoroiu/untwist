package ro.sixi.eval.util;

import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

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
    private final static long multiplier = 0x08088405L;
    private final static long invmultiplier = 0xD94FA8CDL;
    private final static long addend = 0x1L;
    private final static long mask = (1L << 32) - 1;

    private double nextGaussian = Double.NaN;
    private long seed;

    public TurboPascalRandom(int seed) {
        setSeed(seed);
    }

    public TurboPascalRandom(long seed) {
        setSeed(seed);
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

    private void prevSeed() {
        seed = ((seed - addend) * invmultiplier) & mask;
    }

    private void nextSeed() {
        seed = (seed * multiplier + addend) & mask;
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

    public int prevInt() {
        int result = (int) seed;
        prevSeed();
        return result;
    }

    @Override
    public long nextLong() {
        return ((long) (nextInt()) << 32) + nextInt();
    }

    @Override
    public boolean nextBoolean() {
        return nextInt(2) == 0;
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
    @Deprecated
    public float nextFloat() {
        throw new UnsupportedOperationException(
                "nextFloat - python supports only double precision floating point numbers");
    }

    /**
     * {@link MersenneTwisterPy3kCompat#nextBytes(byte[])}
     */
    @Override
    public void nextBytes(byte[] bytes) {
        int i = 0;
        final int iEnd = bytes.length - 4;
        while (i < iEnd) {
            final int random = nextInt();
            bytes[i] = (byte) (random & 0xff);
            bytes[i + 1] = (byte) ((random >> 8) & 0xff);
            bytes[i + 2] = (byte) ((random >> 16) & 0xff);
            bytes[i + 3] = (byte) ((random >> 24) & 0xff);
            i += 4;
        }
        int random = nextInt();
        final int shift = 32 - (bytes.length - i) * 8;
        random >>>= shift;
        while (i < bytes.length) {
            bytes[i++] = (byte) (random & 0xff);
            random >>= 8;
        }
    }

    /**
     * {@link BitsStreamGenerator#nextGaussian}
     */
    @Override
    public double nextGaussian() {
        final double random;
        if (Double.isNaN(nextGaussian)) {
            // generate a new pair of gaussian numbers
            final double x = nextDouble();
            final double y = nextDouble();
            final double alpha = 2 * FastMath.PI * x;
            final double r = FastMath.sqrt(-2 * FastMath.log(y));
            random = r * FastMath.cos(alpha);
            nextGaussian = r * FastMath.sin(alpha);
        } else {
            // use the second element of the pair already generated
            random = nextGaussian;
            nextGaussian = Double.NaN;
        }
        return random;
    }
}
