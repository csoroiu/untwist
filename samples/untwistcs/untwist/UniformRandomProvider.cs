namespace org.apache.commons.rng
{
    public interface UniformRandomProvider
    {
        void NextBytes(byte[] bytes);
        void NextBytes(byte[] bytes, int start, int len);
        int NextInt();
        int NextInt(int bound);
        long NextLong();
        long NextLong(long bound);
        bool NextBoolean();
        float NextFloat();
        double NextDouble();
    }
}