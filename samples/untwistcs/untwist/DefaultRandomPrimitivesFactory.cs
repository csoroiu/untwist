using System;
using org.apache.commons.rng;

namespace ro.derbederos.untwist
{
    internal static class DefaultRandomPrimitivesFactory
    {
        public static int NextInt(UniformRandomProvider generator, int bound)
        {
            if (bound <= 0)
            {
                throw new ArgumentException("bound must be strictly positive");
            }

            uint r = ((uint)generator.NextInt()) >> 1;
            int m = bound - 1;
            if ((bound & m) == 0)  // i.e., bound is a power of 2
                r = (uint)((bound * (long)r) >> 31);
            else
            {
                // reject over-represented candidates
                uint u = r;
                while (u - (r = u % (uint)bound) + m < 0)
                {
                    u = ((uint)generator.NextInt()) >> 1;
                }
            }
            return (int)r;
        }

        public static int NextInt(UniformRandomProvider generator, int origin, int bound)
        {
            if (origin < bound)
            {
                int n = bound - origin;
                if (n > 0)
                {
                    return generator.NextInt(n) + origin;
                }
                else
                {  // range not representable as int
                    int r;
                    do
                    {
                        r = generator.NextInt();
                    } while (r < origin || r >= bound);
                    return r;
                }
            }
            else
            {
                return generator.NextInt();
            }
        }
        public static long NextLong(UniformRandomProvider generator, long bound)
        {
            if (bound <= 0)
            {
                throw new ArgumentException("bound must be strictly positive");
            }

            long r = generator.NextLong();
            long m = bound - 1;
            if ((bound & m) == 0)
            {  // i.e., bound is a power of 2
                r = r & m;
            }
            else
            {
                // reject over-represented candidates
                long u = r >> 1;
                while (u + m - (r = u % bound) < 0L)
                {
                    u = generator.NextLong() >> 1;
                }
            }
            return r;
        }

    }
}
