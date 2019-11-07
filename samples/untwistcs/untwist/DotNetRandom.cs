using System;
using org.apache.commons.rng;

namespace ro.derbederos.untwist
{
    public class DotNetRandom : Random, UniformRandomProvider
    {

        public DotNetRandom(int seed) : base(seed) { }

        public virtual bool NextBoolean() => Convert.ToBoolean(Next(2));

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
            for (int i = start; i < start + len; i++)
            {
                bytes[i] = (byte)(NextInt() % (1 << 8));
            }
        }

        public virtual float NextFloat() => (float)NextDouble();

        public int NextInt() => Next();

        public int NextInt(int bound) => Next(bound);

        public virtual long NextLong()
        {
            long b1 = this.NextInt() & 0xFFFFL; // 16 bits
            long b2 = this.NextInt() & 0xFFFFFFL; // 24 bits
            long b3 = this.NextInt() & 0xFFFFFFL; // 24 bits
            return b1 << 48 | b2 << 24 | b3;
        }

        public virtual long NextLong(long bound) => DefaultRandomPrimitivesFactory.NextLong(this, bound);
    }
}