using System;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace ro.derbederos.untwist
{
    [TestClass]
    public class DotNetRandomTest :
        ReverseUniformRandomProviderAbstractTest<DotNetRandom>
    {
        public override DotNetRandom MakeGenerator()
        {
            return new DotNetRandom(-0x3f97396e);
        }

        [TestMethod]
        public override void TestNextIntIAE2()
        {
            try
            {
                generator.NextInt(-1);
                Assert.Fail("ArgumentException expected");
            }
            catch (ArgumentException)
            {
            }
            int nextInt = generator.NextInt(0);
            Assert.AreEqual(nextInt, 0);
        }

        [TestMethod]
        [Ignore("DotNetRandom generates only positive values")]
        public override void TestNextInt2()
        {
            base.TestNextInt2();
        }

        [TestMethod]
        public override void TestNextIntWideRangeExactValue()
        {
            int[] expected = {72659689, 479570516, 1964597322, 1152315378, 835159700, 1400239888, 824890196,
                -84625816, 934767641, 1787676899, -950803732, -984901899, 1160627160, 854462981,
                -375650433, 751425218, -869369328, -311654904, 1868174591, 1892969089};
            int[] actual = generator.NextInts(expected.Length, -1_000_000_000, int.MaxValue).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextInt16ExactValue()
        {
            int[] expected = { 5, 10, 0, 10, 14, 0, 5, 1, 2, 4 };
            int[] actual = generator.NextInts(expected.Length, 0, 16).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextIntExactValue()
        {
            int[] expected = {683762528, 1373746042, 128503505, 1360364556, 1897922134,
                90664323, 789505134, 235596161, 356723453, 608884583};
            int[] actual = generator.NextInts(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        [Ignore("NextLong(int) not implemented.")]
        public override void TestNextLong16ExactValue()
        {
            Assert.IsTrue(false, "NextLong(int) not implemented.");
        }

        [TestMethod]
        public override void TestNextLongExactValue()
        {
            long[] expected = {7160971568492367569L, -8931728783750763133L, -1986638387611489539L, -2925306645117407511L,
                6258300555690268542L, -554966206143487861L, 3824004281963357759L, -2002195686033151106L,
                -777744680147937180L, 7928930161609697530L};
            long[] actual = generator.NextLongs(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextDoubleExactValue()
        {
            double[] expected = {0.31840173914954145, 0.63970035064951536, 0.059839107589721267, 0.63346911064976319,
                0.88378886453983785, 0.042218865380724363, 0.36764197720570579, 0.10970801166710817,
                0.16611230241419389, 0.28353397887364679};
            double[] actual = generator.NextDoubles(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextFloatExactValue()
        {
            float[] expected = {0.31840175F, 0.63970035F, 0.059839107F, 0.6334691F, 0.8837889F,
                0.042218864F, 0.367642F, 0.10970801F, 0.1661123F, 0.283534F};
            float[] actual = generator.NextFloats(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }

        [TestMethod]
        public override void TestNextBooleanExactValue()
        {
            bool[] expected = {false, true, false, true, true, false, false, false, false, false,
                true, false, false, true, false, true, false, true, true, false};
            bool[] actual = generator.NextBooleans(expected.Length).ToArray();

            CollectionAssert.AreEqual(actual, expected);
        }
    }
}
