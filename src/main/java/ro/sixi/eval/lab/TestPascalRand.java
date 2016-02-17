package ro.sixi.eval.lab;

import ro.sixi.eval.util.MersenneTwisterPy3kCompat;
import ro.sixi.eval.util.TurboPascalRandom;

public class TestPascalRand {

    public static TurboPascalRandom r = new TurboPascalRandom(0);

    public static void randDoubleSingle(long seed) {
        r.setSeed(seed);
        System.out.printf("%.16f %d\n", r.nextDouble(), r.getSeed());
    }

    public static void randDoubleInterval(int start_seed, int end_seed) {
        for (long i = start_seed; i <= end_seed; i++) {
            randDoubleSingle(i);
        }
    }

    public static void randIntSingle(long seed, int range) {
        r.setSeed(seed);
        System.out.printf("%d %d\n", r.nextInt(range), r.getSeed());
    }

    public static void randIntInterval(int start_seed, int end_seed, int range) {
        for (long i = start_seed; i <= end_seed; i++) {
            randIntSingle((int) i, range);
        }
    }

    private static void testNextDoubleFreePascal() {
        // FreePascal random function works similar with nextFloat
        // but with different precision
        // Also seed is used similar to setSeed(int) which is deprecated
        MersenneTwisterPy3kCompat r = new MersenneTwisterPy3kCompat(0);
        r.setSeed(1234567890);
        for (int i = 0; i < 20; i++) {
            System.out.printf("%.16f\n", r.nextDouble());
        }
    }

    public static void main(String[] args) {
        // randDoubleInterval(9974, 10000);
        // randDoubleSingle(0);
        testNextDoubleFreePascal();
    }
}
