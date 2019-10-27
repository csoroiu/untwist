using System;
using System.Globalization;
using System.Threading;

namespace ro.derbederos.untwist
{
    internal static class randcs
    {
        private static void EnableCulture(string name)
        {
            try
            {
                var culture = CultureInfo.GetCultureInfo(name);
                Thread.CurrentThread.CurrentCulture = culture;
                Thread.CurrentThread.CurrentUICulture = culture;
                CultureInfo.DefaultThreadCurrentCulture = culture;
                CultureInfo.DefaultThreadCurrentUICulture = culture;
            }
            catch (CultureNotFoundException)
            {
                //not doing anything
            }
        }

        public static void Main(string[] args)
        {
            EnableCulture("en-US");

            TestDouble(-0x3f97396e);
            TestInt(-0x3f97396e);
        }

        private static void TestInt(int seed)
        {
            var generator = new Random(seed);
            for (var i = 0; i < 20; i++)
            {
                Console.WriteLine(generator.Next(-1_000_000_000, int.MaxValue));
            }
        }

        private static void TestDouble(int seed)
        {
            var generator = new Random(seed);
            for (var i = 0; i < 10; i++)
            {
                var d = generator.NextDouble();
                var s = d.ToString("R"); //round-trip format
                Console.WriteLine(s);
            }
        }

        private static string ToHexBinaryString(double d)
        {
            return Convert.ToString(BitConverter.DoubleToInt64Bits(d), 16);
        }
    }
}
