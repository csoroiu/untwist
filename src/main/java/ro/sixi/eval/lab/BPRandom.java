package ro.sixi.eval.lab;

/*
 * http://stackoverflow.com/questions/3946869/how-reliable-is-the-random-function-in-delphi#3953956
 * http://labs.icb.ufmg.br/lbcd/prodabi5/homepages/hugo/Hugo/TPB/RTL/SYS/RAND.ASM
 * http://people.cs.nctu.edu.tw/~tsaiwn/sisc/runtime_error_200_div_by_0/www.merlyn.demon.co.uk/#pas
 * http://forum.lazarus.freepascal.org/index.php?topic=2665.0
 * http://virtualschool.edu/mon/Crypto/RandomNumberMath
 */
public class BPRandom {
    long seed;

    public BPRandom(int seed) {
        this.seed = seed;
    }

    private void nextSeed() {
        seed = (seed * 0x08088405L + 1) & 0xFFFFFFFFL;
    }

    private void prevSeed() {
        seed = ((seed - 1) * 0xD94FA8CDL) & 0xFFFFFFFFL;
    }

    // RandSeed = -1498392781 precedes 0
    // http://www.efg2.com/Lab/Library/Delphi/MathFunctions/random.txt - _randExt
    public double nextDouble() {
        nextSeed();
        double value = seed / (double) (1L << 32);
        // hack to adjust the value to match the stuff from pascal
        // TODO figure out how borland pascal works for generating floats
        // there is a difference when using $N+ or $N-
        if (value >= .5f)
            value -= .5f;
        else
            value += .5f;
        return value;
    }

    public int nextInt(int n) {
        nextSeed();
        int result = (int) ((seed * n) >>> 32);
        // if (result < 0) {
        // result += n;
        // }
        return result;
    }

    public static void main(String[] args) {
        // int seed = 7, n = 10, m = 3, k = 40, div = 1; // test 6
        // int seed = 7, n = 22, m = 77, k = 7, div = 1; // test 7
        // int seed = 7, n = 53, m = 53, k = 53, div = 1; // test 8
        // int seed = 5, n = 77, m = 67, k = 76, div = 5; // test 9
        int seed = 5, n = 99, m = 79, k = 96, div = 5; // test 10
        BPRandom r = new BPRandom(seed);

        System.out.println(n + " " + m + " " + k);
        for (int i = 0; i < n; i++) {
            final int value1 = (int) (r.nextDouble() * 1000000);
            final int value2 = r.nextInt(m / div) + 1;
            final int value3 = r.nextInt(k / div) + 1;
            System.out.println(value1 + " " + value2 + " " + value3);
        }
    }
}
