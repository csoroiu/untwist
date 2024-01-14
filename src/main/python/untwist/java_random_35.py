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
from typing import List


def _signed_int(value: int) -> int:
    """
    The method makes sure that our 32bit int is signed so we negate it
    """
    if value & 0x80000000:
        value -= 0x100000000
    return value


class JavaRandom(object):
    """
    https://github.com/MostAwesomeDude/java-random

    An implementation of the Java SE random number generator.
    Java's RNG is based on a classic Knuth-style linear congruential formula,
    as described in
    http://docs.oracle.com/javase/8/docs/api/java/util/Random.html. This
    makes it quite trivial to re-implement and port to other platforms.
    This class should be bit-for-bit compatible with any Java RNG.
    This class is not thread-safe. For deterministic behaviour, lock or
    synchronize all accesses to this class per-instance.
    """

    __seed_uniquifier = 8682522807148012

    @staticmethod
    def __next_seed_uniquifier() -> int:
        val = JavaRandom.__seed_uniquifier
        JavaRandom.__seed_uniquifier = (val * 181783497276652981) & 0xFFFFFFFFFFFFFFFF
        return JavaRandom.__seed_uniquifier

    def __init__(self, seed: int = None) -> None:
        """Create a new random number generator."""

        if seed is None:
            micros = int(time.monotonic() * 1E6)
            seed = micros ^ JavaRandom.__next_seed_uniquifier()
        self.__seed = 0
        self.set_seed(seed)
        self.__next_next_gaussian = None

        # method redefinition
        self.random = self.next_double
        self.randbool = self.next_boolean
        self._randbelow = self.next_int
        self.seed = self.set_seed

    def get_seed(self) -> int:
        """
        Returns the seed value. The returned value can be used to build a new generator
        that will return the same values as the current one.
        """
        return (self.__seed ^ 0x5deece66d) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)

    def set_seed(self, seed: int) -> None:
        """Sets the seed for the generator."""
        self.__seed = (seed ^ 0x5deece66d) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)

    def __next(self, bits: int) -> int:
        """
        Generate the next random number.
        As in Java, the general rule is that this method returns an int that
        is `bits` bits long, where each bit is nearly equally likely to be 0
        or 1.
        """

        self.__seed = (self.__seed * 0x5deece66d + 0xb) & 0xFFFFFFFFFFFF  # ((1 << 48) - 1)
        retval = self.__seed >> (48 - bits)

        return _signed_int(retval)

    def next_bytes(self, l: List[int]) -> None:
        """Replace every item in `l` with a random byte."""

        n = 0
        for i in range(0, len(l)):
            if not i & 3:  # not i % 4
                n = self.next_int()
            b = n & 0xFF
            # Flip signs. Ugh.
            if b & 0x80:
                b -= 0x100
            l[i] = b
            n >>= 8

    def next_int(self, n: int = None) -> int:
        """
        Return a random int in [0, `n`).
        If `n` is not supplied, a random 32-bit integer will be returned.
        """

        if n is None:
            return self.__next(32)

        if n <= 0 or n > 0x7FFFFFFF:
            raise ValueError("Argument must be 32 bit signed integer with positive value!")

        # This tricky chunk of code comes straight from the Java spec. In
        # essence, the algorithm tends to have much better entropy in the
        # higher bits of the seed, so this little bundle of joy is used to try
        # to reject values which would be obviously biased. We do have an easy
        # out for power-of-two n, in which case we can call next directly.

        # Is this a power of two?
        if not (n & (n - 1)):
            return (n * self.__next(31)) >> 31

        bits = self.__next(31)
        val = bits % n
        while _signed_int(bits - val + (n - 1)) < 0:
            bits = self.__next(31)
            val = bits % n

        return val

    def next_long(self) -> int:
        """
        Return a random long.
        Java longs are 64 bits wide, but the generator is only 48 bits wide,
        so we generate two 32-bit numbers and glue them together.
        """

        return (self.__next(32) << 32) + self.__next(32)

    def next_boolean(self) -> bool:
        """Return a random bool."""

        return bool(self.__next(1))

    def next_float(self) -> float:
        """
        Return a random float in (0, 1).
        Python floats always carry double precision, so this function's return
        values may appear less-than-random, but they are random in single
        precision space.
        """

        return self.__next(24) / float(1 << 24)

    def next_double(self) -> float:
        """Return a random float in (0, 1)."""

        return ((self.__next(26) << 27) + self.__next(27)) / float(1 << 53)

    def getrandbits(self, k: int) -> int:
        byte_arr = [0] * ((k + 7) // 8)
        self.next_bytes(byte_arr)
        shift = k & 7
        byte_arr[-1] >>= shift
        return int.from_bytes(byte_arr, 'little')

    def randrange(self, start: int, stop: int = None) -> int:
        """Choose a random item from range(start, stop[, step])."""
        if stop is None:
            return self._randbelow(start)
        return start + self._randbelow(stop - start)

    def randint(self, start: int, stop: int) -> int:
        """Return random integer in range [a, b], including both end points."""

        return self.randrange(start, stop + 1)
