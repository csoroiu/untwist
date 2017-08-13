using System;
using System.Globalization;

namespace Program
{
    class randcs
    {
        static void Main(string[] args)
        {
            CultureInfo.DefaultThreadCurrentCulture = new CultureInfo("en-US");
            testDouble();
        }

        static void testInt()
        {
            Random generator = new Random(-0x3f97396e);
            for (int i = 0; i < 20; i++)
            {
                Console.WriteLine(generator.Next(-1_000_000_000, Int32.MaxValue));
            }
        }

        static void testDouble()
        {
            Random generator = new Random(-0x3f97396e);
            double[] expected = {0.31840173914954145, 0.6397003506495154, 0.05983910758972127, 0.6334691106497632,
                0.8837888645398378, 0.04221886538072436, 0.3676419772057058, 0.10970801166710817,
                0.1661123024141939, 0.2835339788736468};

            for(int i = 0; i < 10; i++)
            {
                double d = generator.NextDouble();
                string s = d.ToString("R"); //round-trip format
                Console.WriteLine(s);
            }
        }

        static string toHexBinaryString(double d) {
            return Convert.ToString(BitConverter.DoubleToInt64Bits(d), 16);
        }
    }
}