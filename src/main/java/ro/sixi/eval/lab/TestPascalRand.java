package ro.sixi.eval.lab;

import ro.sixi.eval.random.DotNetRandom;
import ro.sixi.eval.random.MersenneTwisterPy3kCompat;
import ro.sixi.eval.random.TurboPascalRandom;

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
        // random := cardinal(genrand_MT19937) * (extended(1.0)/(int64(1) shl 32));
        // random := mtwist_u32rand * (extended(1.0)/(int64(1) shl 32));
        // but with different precision
        // Also seed is used similar to setSeed(int) which is deprecated
        MersenneTwisterPy3kCompat r = new MersenneTwisterPy3kCompat(0);
        r.setSeed(1234567890);
        for (int i = 0; i < 20; i++) {
            System.out.printf("%.16f\n", r.nextDouble());
        }
    }

    private static void testDotNetInt() {
        DotNetRandom r = new DotNetRandom(1234567890);
        for (int i = 0; i < 10; i++) {
            System.out.printf("%d\n", r.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE));
        }
    }

    private static void testDotNetDouble() {
        DotNetRandom r = new DotNetRandom(1234567890);
        for (int i = 0; i < 1000; i++) {
            r.nextDouble();
        }
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.17f\n", r.nextDouble());
        }
    }

    public static void main(String[] args) {
        // Mono.net random - https://github.com/mono/mono/blob/master/mcs/class/Mono.C5/C5/Random.cs
        // Mscorlib.net random - http://referencesource.microsoft.com/#mscorlib/system/random.cs

        // freepascal https://github.com/graemeg/freepascal/blob/master/rtl/inc/system.inc
        // https://github.com/graemeg/freepascal/blob/master/rtl/inc/systemh.inc

        // randDoubleInterval(9974, 10000);
        // randDoubleSingle(0);
//        testNextDoubleFreePascal();
        testDotNetDouble();
    }

}
