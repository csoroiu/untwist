using System;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using org.apache.commons.rng;

namespace ro.derbederos.untwist
{
    [TestClass]
    public abstract class ReverseUniformRandomProviderAbstractTest<T>
                    where T : Random, UniformRandomProvider
    {
        protected T generator;

        [TestInitialize]
        public virtual void SetUp()
        {
            generator = MakeGenerator();
        }

        public abstract T MakeGenerator();

        [TestMethod]
        public virtual void TestNextInt2()
        {
            int N = 10000;
            int positives = (int)RandomUtils.NextInts(generator, N).Where(value => value >= 0).Count();
            int walk = 2 * positives - N;
            Assert.IsTrue(Math.Abs(walk) < Math.Sqrt(N) * 2.576,
                            "Walked too far astray: " + walk + "\nNote: This " +
                            "test will fail randomly about 1 in 100 times.");
        }

        [TestMethod]
        public virtual void TestNextLong2()
        {
            int N = 10000;
            int positives = (int)RandomUtils.NextLongs((UniformRandomProvider)generator, N).Where(value => value >= 0).Count();
            int walk = 2 * positives - N;
            Assert.IsTrue(Math.Abs(walk) < Math.Sqrt(N) * 2.576,
                            "Walked too far astray: " + walk + "\nNote: This " +
                            "test will fail randomly about 1 in 100 times.");
        }

        [TestMethod]
        public virtual void TestNexBoolean2()
        {
            int N = 10000;
            int positives = (int)RandomUtils.NextBooleans((UniformRandomProvider)generator, N).Where(value => value).Count();
            int walk = 2 * positives - N;
            Assert.IsTrue(Math.Abs(walk) < Math.Sqrt(N) * 2.576,
                            "Walked too far astray: " + walk + "\nNote: This " +
                            "test will fail randomly about 1 in 100 times.");
        }

        [TestMethod]
        public virtual void TestNextIntWideRange()
        {
            int lower = -0x6543210F;
            int upper = 0x456789AB;
            var ints = RandomUtils.NextInts(generator, 1_000_000, lower, upper).ToArray();
            int min = ints.Min();
            int max = ints.Max();

            Assert.IsTrue(min >= lower);
            Assert.IsTrue(max <= upper);
            double ratio = ((double)max - (double)min) /
                    (((double)upper) - ((double)lower));
            Assert.IsTrue(ratio > 0.99999);
        }

        [TestMethod]
        public virtual void TestNextIntIAE2()
        {
            try
            {
                generator.NextInt(-1);
                Assert.Fail("ArgumentException expected");
            }
            catch (ArgumentException)
            {
            }
            try
            {
                generator.NextInt(0);
                Assert.Fail("ArgumentException expected");
            }
            catch (ArgumentException)
            {
            }
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentException), AllowDerivedTypes = true)]
        public void testNextIntNeg()
        {
            generator.NextInt(-1);
        }

        [TestMethod]
        public abstract void TestNextInt16ExactValue();

        [TestMethod]
        public abstract void TestNextIntExactValue();

        [TestMethod]
        public abstract void TestNextIntWideRangeExactValue();

        [TestMethod]
        public abstract void TestNextLong16ExactValue();

        [TestMethod]
        public abstract void TestNextLongExactValue();

        [TestMethod]
        public abstract void TestNextDoubleExactValue();

        [TestMethod]
        public abstract void TestNextFloatExactValue();

        [TestMethod]
        public abstract void TestNextBooleanExactValue();

    }
}