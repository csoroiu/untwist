using System;
using System.Threading;
using System.Diagnostics;
using org.apache.commons.rng;

namespace ro.derbederos.untwist
{
    public class JavaRandom : Random, UniformRandomProvider
    {
        private const long MULTIPLIER = 0x5DEECE66DL;
        private const long INVERSE_MULTIPLIER = 0xDFE05BCB1365L;
        private const long ADDEND = 0xBL;
        private const long MASK = 0xFFFFFFFFFFFFL;            // (1L << 48) - 1
        private const double DOUBLE_UNIT = 1.0d / (1L << 53); // 1.0 / (1L << 53)
        private const float FLOAT_UNIT = 1.0f / (1 << 24);    // 1.0 / (1 << 24)

        private static long seedUniquifier = 8682522807148012L;
        private long seed;

        public JavaRandom()
        {
            SetSeed(NextSeedUniquifier() ^ Stopwatch.GetTimestamp());
        }

        private static long NextSeedUniquifier()
        {
            // L'Ecuyer, "Tables of Linear Congruential Generators of
            // Different Sizes and Good Lattice Structure", 1999
            for (; ; )
            {
                long current = Thread.VolatileRead(ref seedUniquifier);
                long next = current * 1181783497276652981L;
                if (current == Interlocked.CompareExchange(ref seedUniquifier, next, current))
                    return next;
            }
        }
        public JavaRandom(long seed)
        {
            SetSeed(seed);
        }
        public virtual void SetSeed(long seed)
        {
            Thread.VolatileWrite(ref this.seed, ScrambleSeed(seed));
        }
        private static long ScrambleSeed(long seed) => (seed ^ MULTIPLIER) & MASK;
        protected virtual int NextBits(int bits)
        {
            long oldSeed, nextSeed;
            do
            {
                oldSeed = Thread.VolatileRead(ref this.seed);
                nextSeed = (oldSeed * MULTIPLIER + ADDEND) & MASK;
            } while (oldSeed != Interlocked.CompareExchange(ref this.seed, nextSeed, oldSeed));
            return (int)(((ulong)nextSeed) >> (48 - bits));
        }
        protected override sealed double Sample() => NextDouble();
        public override sealed int Next() => NextInt();
        public override sealed int Next(int minValue, int maxValue) => DefaultRandomPrimitivesFactory.NextInt(this, minValue, maxValue);
        public virtual int NextInt() => NextBits(32);
        public virtual int NextInt(int bound) => DefaultRandomPrimitivesFactory.NextInt(this, bound);
        public virtual long NextLong() => ((long)(NextBits(32)) << 32) + NextBits(32);
        public virtual long NextLong(long bound) => DefaultRandomPrimitivesFactory.NextLong(this, bound);
        public override double NextDouble() => (((long)(NextBits(26)) << 27) + NextBits(27)) * DOUBLE_UNIT;
        public virtual float NextFloat() => NextBits(24) / ((float)(1 << 24));
        public virtual bool NextBoolean() => NextBits(1) != 0;
        public override void NextBytes(byte[] bytes) => NextBytesFill(bytes, 0, bytes.Length);
        public virtual void NextBytes(byte[] bytes, int start, int len)
        {
            if (start < 0 || start >= bytes.Length)
            {
                throw new ArgumentOutOfRangeException("start", start, "less than 0 or greater than source byte array");
            }
            if (len < 0 || len > bytes.Length - start)
            {
                throw new ArgumentOutOfRangeException("len", len, "out of range, starting from start parameter");
            }

            NextBytesFill(bytes, start, len);
        }
        private void NextBytesFill(byte[] bytes, int start, int len)
        {
            if (bytes == null)
            {
                throw new NullReferenceException("bytes");
            }
            for (int i = start, end = start + len; i < end;)
            {
                for (int rnd = NextInt(),
                         n = Math.Min(end - i, sizeof(int) / sizeof(byte));
                     n-- > 0; rnd >>= sizeof(byte))
                {
                    bytes[i++] = (byte)rnd;
                }
            }
        }
    }
}