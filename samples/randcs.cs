using System;
using System.Globalization;
using System.Threading;

namespace Program
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
            TestDouble();
            TestInt();
        }

        private static void TestInt()
        {
            var generator = new Random(-0x3f97396e);
            for (var i = 0; i < 20; i++)
            {
                Console.WriteLine(generator.Next(-1_000_000_000, int.MaxValue));
            }
        }

        private static void TestDouble()
        {
            var generator = new Random(-0x3f97396e);
            double[] expected =
            {
                0.31840173914954145, 0.6397003506495154, 0.05983910758972127, 0.6334691106497632,
                0.8837888645398378, 0.04221886538072436, 0.3676419772057058, 0.10970801166710817,
                0.1661123024141939, 0.2835339788736468
            };

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
