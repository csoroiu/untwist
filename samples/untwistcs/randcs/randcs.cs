using System;
using System.Globalization;
using System.Threading;

namespace ro.derbederos.untwist
{
    internal static class randcs
    {
        public static void Main(string[] args)
        {
            EnableCulture("en-US");
            if (args.Length < 1 || args.Length > 2)
            {
                Console.WriteLine("Invalid number of arguments");
                Console.WriteLine("Usage: randcs seed - generates one double in the interval [0, 1) and one int");
                Console.WriteLine("Usage: randcs seed count - generates <count> doubles in the interval [0, 1) and <count> ints");
                System.Environment.Exit(1);
            }
            int seed = 0;
            int count = 1;
            try
            {
                seed = ParseInt(args[0]);
                if (args.Length == 2)
                {
                    count = ParseInt(args[1]);
                }
            }
            catch (FormatException ex)
            {
                Console.WriteLine(ex.ToString());
                System.Environment.Exit(2);
            }
            catch (OverflowException ex)
            {
                Console.WriteLine(ex.ToString());
                System.Environment.Exit(2);
            }
            GenerateDouble(seed, count);
            GenerateInt(seed, count);
        }
        private static void GenerateDouble(int seed, int count)
        {
            var generator = new Random(seed);
            for (var i = 0; i < count; i++)
            {
                var d = generator.NextDouble();
                var s = d.ToString("R"); //round-trip format
                Console.WriteLine(s + " " + ToHexBinaryString(d));
            }
        }
        private static void GenerateInt(int seed, int count)
        {
            var generator = new Random(seed);
            for (var i = 0; i < count; i++)
            {
                Console.WriteLine(generator.Next());
            }
        }
        private static string ToHexBinaryString(double d)
        {
            return Convert.ToString(BitConverter.DoubleToInt64Bits(d), 16);
        }
        private static int ParseInt(String s)
        {
            if (s.ToLower().StartsWith("0x"))
            {
                return Convert.ToInt32(s, 16);
            }
            else
            {
                return Convert.ToInt32(s, 10);
            }
        }
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
    }
}
