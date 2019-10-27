using System;
using System.Collections.Generic;

namespace ro.derbederos.untwist
{
    public static class RandomUtils
    {
        /// [Random missing functions
        public static long NextLong(this Random generator)
        {
            long b1 = generator.Next() & 0xFFFFL; // 16 bits
            long b2 = generator.Next() & 0xFFFFFFL; // 24 bits
            long b3 = generator.Next() & 0xFFFFFFL; // 24 bits
            return b1 << 48 | b2 << 24 | b3;
        }

        public static float NextFloat(this Random generator)
            => (float)generator.NextDouble();

        public static bool NextBoolean(this Random generator)
            => Convert.ToBoolean(generator.Next(2));
        /// ]

        /// [Random functions from java
        public static IEnumerable<int> NextInts(this Random generator)
            => Generate(generator.Next);

        public static IEnumerable<int> NextInts(this Random generator, long streamSize)
            => NextInts(generator).TakeLong(streamSize);

        public static IEnumerable<int> NextInts(this Random generator, int origin, int bound)
            => Generate(() => generator.Next(origin, bound));

        public static IEnumerable<int> NextInts(this Random generator, long streamSize, int origin, int bound)
            => NextInts(generator, origin, bound).TakeLong(streamSize);

        public static IEnumerable<long> NextLongs(this Random generator)
            => Generate(generator.NextLong);

        public static IEnumerable<long> NextLongs(this Random generator, long streamSize)
            => NextLongs(generator).TakeLong(streamSize);

        public static IEnumerable<float> NextFloats(this Random generator)
            => Generate(generator.NextFloat);

        public static IEnumerable<float> NextFloats(this Random generator, long streamSize)
            => NextFloats(generator).TakeLong(streamSize);

        public static IEnumerable<double> NextDoubles(this Random generator)
            => Generate(generator.NextDouble);

        public static IEnumerable<double> NextDoubles(this Random generator, long streamSize)
            => NextDoubles(generator).TakeLong(streamSize);

        public static IEnumerable<bool> NextBooleans(this Random generator)
            => Generate(generator.NextBoolean);

        public static IEnumerable<bool> NextBooleans(this Random generator, long streamSize)
            => NextBooleans(generator).TakeLong(streamSize);

        static IEnumerable<TSource> TakeLong<TSource>(this IEnumerable<TSource> source, long count)
        {
            if (source == null)
            {
                throw new NullReferenceException();
            }
            return TakeLongIterator<TSource>(source, count);
        }

        private static IEnumerable<TSource> TakeLongIterator<TSource>(IEnumerable<TSource> source, long count)
        {
            if (count > 0)
            {
                foreach (TSource element in source)
                {
                    yield return element;
                    if (--count == 0) break;
                }
            }
        }
        private static IEnumerable<TSource> Generate<TSource>(Func<TSource> func)
        {
            while (true)
            {
                yield return func();
            }
        }
    }
}
