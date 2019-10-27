using System;
using System.Security.Cryptography;

namespace ro.derbederos.untwist
{
    public static class SeedUtils
    {
        public volatile static Lazy<RandomNumberGenerator> SeedGenerator =
            new Lazy<RandomNumberGenerator>(() => new RNGCryptoServiceProvider());

        public static int GenerateSecureRandomIntSeed()
            => BitConverter.ToInt32(GenerateSecureRandomByteArraySeed(sizeof(int)), 0);

        public static long GenerateSecureRandomLongSeed()
            => BitConverter.ToInt64(GenerateSecureRandomByteArraySeed(sizeof(long)), 0);

        public static int[] GenerateSecureRandomIntArraySeed(int size)
        {
            var randomBytes = GenerateSecureRandomByteArraySeed(size * sizeof(int));
            var randomInts = new int[size];
            Buffer.BlockCopy(randomBytes, 0, randomInts, 0, randomBytes.Length);
            return randomInts;
        }

        private static byte[] GenerateSecureRandomByteArraySeed(int size)
        {
            byte[] randomBytes = new byte[size];
            SeedGenerator.Value.GetBytes(randomBytes);
            return randomBytes;
        }
    }
}
