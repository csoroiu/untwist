using System;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using org.apache.commons.rng;

namespace ro.derbederos.untwist
{
    [TestClass]
    public class JavaRandomTest : ReverseUniformRandomProviderAbstractTest<JavaRandom>
    {
        public override JavaRandom MakeGenerator()
        {
            return new JavaRandom(1000);
        }

        [TestMethod]
        public void testDefaultConstructor()
        {
            long firstValue = new JavaRandom().NextLong();
            long secondValue = new JavaRandom().NextLong();

            Assert.AreNotEqual(firstValue, secondValue);
        }

        [TestMethod]
        public override void TestNextInt16ExactValue()
        {
            int[] expected = { 11, 3, 9, 7, 15, 0, 0, 13, 7, 5 };
            int[] actual = generator.NextInts(expected.Length, 0, 16).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public virtual void TestNextInt255ExactValue()
        {
            int[] expected = { 112, 190, 246, 34, 177, 179, 106, 230, 254, 90 };
            int[] actual = generator.NextInts(expected.Length, 0, 255).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextIntWideRangeExactValue()
        {
            int[] expected = {1060493871, 1976922248, -230127712, 68408698, 169247282, -735843605, 2089114528,
                1533708900, 1914424759, 186842318, 1764582767, 36964004, -109536649, 1518828482,
                -648782117, -153007291, 1530083386, 584942498, 1009710484, -97496543};
            int[] actual = generator.NextInts(expected.Length, -1_000_000_000, int.MaxValue).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }


        [TestMethod]
        public override void TestNextIntExactValue()
        {
            int[] expected = {-1244746321, 1060493871, -1826063944, 1976922248, -230127712,
                68408698, 169247282, -735843605, 2089114528, 1533708900};
            int[] actual = generator.NextInts(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextLong16ExactValue()
        {
            long[] expected = { 15L, 8L, 10L, 11L, 4L, 14L, 15L, 6L, 4L, 2L };
            long[] actual = ((UniformRandomProvider)generator).NextLongs(expected.Length, 0, 16).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextLongExactValue()
        {
            long[] expected = {-5346144739450824145L, -7842884917907853176L, -988390996874898054L, 726911540391045867L,
                8972678576892185188L, 8222391730744523982L, -7363680848376404625L, -8294095627538487754L,
                -6307709242837825884L, -470456323649602622L};
            long[] actual = ((UniformRandomProvider)generator).NextLongs(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextDoubleExactValue()
        {
            double[] expected = {0.7101849056320707, 0.574836350385667, 0.9464192094792073, 0.039405954311386604,
                0.4864098780914311, 0.4457367367074283, 0.6008140654988429, 0.550376169584217,
                0.6580583901495688, 0.9744965039734514};
            double[] actual = generator.NextDoubles(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextFloatExactValue()
        {
            float[] expected = {0.7101849F, 0.24691546F, 0.5748363F, 0.46028805F, 0.9464192F,
                0.015927613F, 0.039405942F, 0.828673F, 0.48640984F, 0.3570944F};
            float[] actual = ((UniformRandomProvider)generator).NextFloats(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextBooleanExactValue()
        {
            bool[] expected = {true, false, true, false, true, false, false, true, false, false,
                false, false, true, false, true, true, true, false, true, false};
            bool[] actual = ((UniformRandomProvider)generator).NextBooleans(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }
    }
}
