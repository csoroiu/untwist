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

        public virtual int NextInt() => Next();

        public virtual int NextInt(int bound) => Next(bound);

        public virtual long NextLong()
        {
            long b1 = this.NextInt() & 0xFFFFL; // 16 bits
            long b2 = this.NextInt() & 0xFFFFFFL; // 24 bits
            long b3 = this.NextInt() & 0xFFFFFFL; // 24 bits
            return b1 << 48 | b2 << 24 | b3;
        }

        public virtual long NextLong(long bound)
        {
            if (bound <= 0)
            {
                throw new ArgumentException("bound must be strictly positive");
            }

            long r = this.NextLong();
            long m = bound - 1;
            if ((bound & m) == 0)
            {  // i.e., bound is a power of 2
                r = r & m;
            }
            else
            {
                // reject over-represented candidates
                long u = (long)((ulong)r >> 1);
                while (u + m - (r = u % bound) < 0L)
                {
                    u = (long)((ulong)this.NextLong() >> 1);
                }
            }
            return r;
        }
    }
}