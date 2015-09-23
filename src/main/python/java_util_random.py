"""
This module contains a class that mimics the behaviour of the java.util.Random class.
Inspired from: https://github.com/MostAwesomeDude/java-random
@author: sixi
"""
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals
import time


def _signed_int(x):
    if x & 0x80000000:
        x -= 0x100000000
    return x


class JavaRandom(object):
    """
    An implementation of the Java SE random number generator.
    Java's RNG is based on a classic Knuth-style linear congruential formula,
    as described in
    http://docs.oracle.com/javase/8/docs/api/java/util/Random.html. This
    makes it quite trivial to re-implement and port to other platforms.
    This class should be bit-for-bit compatible with any Java RNG.
    This class is not thread-safe. For deterministic behaviour, lock or
    synchronize all accesses to this class per-instance.
    """

    def __init__(self, seed=None):
        """
        Create a new random number generator.
        """

        if seed is None:
            seed = int(time.time() * 1000)
        self._seed = 0
        self.seed = seed
        self.next_next_gaussian = None

    def set_seed(self, seed):
        """
        Explicit setter for seed, for compatibility with Java.
        """

        self.seed = seed

    @property
    def seed(self):
        return (self._seed ^ 0x5deece66d) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)

    @seed.setter
    def seed(self, seed):
        self._seed = (seed ^ 0x5deece66d) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)

    def next(self, bits):
        """
        Generate the next random number.
        As in Java, the general rule is that this method returns an int that
        is `bits` bits long, where each bit is nearly equally likely to be 0
        or 1.
        """

        if bits < 1:
            bits = 1
        elif bits > 32:
            bits = 32

        self._seed = (self._seed * 0x5deece66d + 0xb) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)
        retval = self._seed >> (48 - bits)

        return _signed_int(retval)

    def next_bytes(self, l):
        """
        Replace every item in `l` with a random byte.
        """

        for i in range(0, len(l)):
            if not i & 3:  # not i % 4
                n = self.next_int()
            b = n & 0xff
            # Flip signs. Ugh.
            if b & 0x80:
                b -= 0x100
            l[i] = b
            n >>= 8

    def next_int(self, n=None):
        """
        Return a random int in [0, `n`).
        If `n` is not supplied, a random 32-bit integer will be returned.
        """

        if n is None:
            return self.next(32)

        if n <= 0:
            raise ValueError("Argument must be positive!")

        # This tricky chunk of code comes straight from the Java spec. In
        # essence, the algorithm tends to have much better entropy in the
        # higher bits of the seed, so this little bundle of joy is used to try
        # to reject values which would be obviously biased. We do have an easy
        # out for power-of-two n, in which case we can call next directly.

        # Is this a power of two?
        if not (n & (n - 1)):
            return (n * self.next(31)) >> 31

        bits = self.next(31)
        val = bits % n
        while _signed_int(bits - val + (n - 1)) < 0:
            bits = self.next(31)
            val = bits % n

        return val

    def next_long(self):
        """
        Return a random long.
        Java longs are 64 bits wide, but the generator is only 48 bits wide,
        so we generate two 32-bit numbers and glue them together.
        """

        return (self.next(32) << 32) + self.next(32)

    def next_boolean(self):
        """
        Return a random bool.
        """

        return bool(self.next(1))

    def next_float(self):
        """
        Return a random float in (0, 1).
        Python floats always carry double precision, so this function's return
        values may appear less-than-random, but they are random in single
        precision space.
        """

        return self.next(24) / float(1 << 24)

    def next_double(self):
        """
        Return a random float in (0, 1).
        """

        return ((self.next(26) << 27) + self.next(27)) / float(1 << 53)
