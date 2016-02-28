package ro.sixi.eval.lab;

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

    public static void main(String[] args) {
        // randDoubleInterval(9974, 10000);
        // randDoubleSingle(0);
    }

}
