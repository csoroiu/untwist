package ro.derbederos.untwist;

/*
 * http://stackoverflow.com/questions/3946869/how-reliable-is-the-random-function-in-delphi#3953956
 * http://labs.icb.ufmg.br/lbcd/prodabi5/homepages/hugo/Hugo/TPB/RTL/SYS/RAND.ASM
 * http://people.cs.nctu.edu.tw/~tsaiwn/sisc/runtime_error_200_div_by_0/www.merlyn.demon.co.uk/#pas
 * http://forum.lazarus.freepascal.org/index.php?topic=2665.0
 * http://virtualschool.edu/mon/Crypto/RandomNumberMath
 * gnu pascal
 * http://latel.upf.edu/morgana/altres/pub/gpc/list2html/1997/mail0911.htm
 * http://answers.unity3d.com/questions/393825/systemrandom-with-seed-not-matching-net-or-mono.html
 */
public class TurboPascalRandom extends ReverseBitsStreamGenerator {
    private static final long serialVersionUID = 1L;

    private final static long multiplier = 0x08088405L;
    private final static long invmultiplier = 0xD94FA8CDL;
    private final static long addend = 0x1L;
    private final static long mask = (1L << 32) - 1;

    /**
     * when enabling coprocessor $N+, in Turbo Pascal 7, the values returned by random function are offset-ed by 0.5
     * from the usual ones. If value is &gt;=0.5 then it is decreased by 0.5 else value is increased by 0.5
     * <p>
     * Delphi maintains compatibility with the $N- version.
     */
    private final boolean coprocEnabled;

    private long seed;

    public TurboPascalRandom(int seed) {
        this(seed, true);
    }

    public TurboPascalRandom(int[] seed) {
        this(seed, true);
    }

    public TurboPascalRandom(long seed) {
        this(seed, true);
    }

    protected TurboPascalRandom(int seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
    }

    protected TurboPascalRandom(int[] seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
    }

    protected TurboPascalRandom(long seed, boolean coprocEnabled) {
        setSeed(seed);
        this.coprocEnabled = coprocEnabled;
    }

    @Override
    @Deprecated
    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToInt(seed));
    }

    @Override
    public void setSeed(long seed) {
        final int high = (int) (seed >>> 32);
        final int low = (int) (seed & 0xffffffffL);
        setSeed(RandomUtils.convertToInt(high, low));
    }

    @Override
    protected int next(int bits) {
        nextSeed();
        return (int) (seed >>> 32 - bits);
    }

    @Override
    protected int prev(int bits) {
        int result = (int) (seed >>> 32 - bits);
        prevSeed();
        return result;
    }

    private void prevSeed() {
        seed = ((seed - addend) * invmultiplier) & mask;
    }

    private void nextSeed() {
        seed = (seed * multiplier + addend) & mask;
    }

    @Override
    public int nextInt(int n) {
        if (n > 0) {
            long nextInt = next(32) & 0xFFFFFFFFL;
            return (int) ((nextInt * n) >>> 32);
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    @Override
    public int prevInt(int n) {
        if (n > 0) {
            long prevInt = prev(32) & 0xFFFFFFFFL;
            return (int) ((prevInt * n) >>> 32);
        }
        throw new IllegalArgumentException("n must be strictly positive");
    }

    @Override
    public long nextLong() {
        return ((long) (nextInt()) << 32) | nextInt();
    }

    @Override
    public long prevLong() {
        return prevInt() | ((long) (prevInt()) << 32);
    }

    // RandSeed = -1498392781 precedes 0
    // http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt - _randExt
    // https://github.com/rofl0r/KOL/blob/master/system/system.pas
    @Override
    public double nextDouble() {
        long nextInt = next(32) & 0xFFFFFFFFL;
        if (coprocEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return (int) nextInt / (double) (1L << 32) + 0.5;
        } else {
            return nextInt / (double) (1L << 32);
        }
    }

    @Override
    public double prevDouble() {
        long prevInt = prev(32) & 0xFFFFFFFFL;
        if (coprocEnabled) {
            // in turbo pascal the seed was 32 bit signed integer
            return (int) prevInt / (double) (1L << 32) + 0.5;
        } else {
            return prevInt / (double) (1L << 32);
        }
    }

    @Override
    @Deprecated
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override
    @Deprecated
    public float prevFloat() {
        return (float) prevDouble();
    }
}
