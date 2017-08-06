from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals
from untwist.random_py3k import RandomPy3k

import struct
import unittest

INT_MAX = (2 ** 31) - 1
UINT_MAX = 2 ** 32
LONG_MAX = (2 ** 63) - 1


def int_from_bytes_list(bytes_list):
    # python3
    # return int.from_bytes(bytes_list, 'big')
    from binascii import hexlify
    return int(hexlify(bytearray(bytes_list)), 16)


class MersenneTwisterPy3kCompatTest(unittest.TestCase):

    def setUp(self):
        self.rand = RandomPy3k(1234567890)


    def test_set_seed_array(self):
        large_seed = int_from_bytes_list([1] * 2499 + [2])
        self.rand.seed(large_seed)
        expected = [360, 239, 640, 729, 558, 92, 366, 913, 108, 132]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(1000))
        self.assertListEqual(actual, expected)


    def test_int_max_value(self):
        expected = [1977150888, 1252380877, 1363867306, 345016483,
                    952454400, 470947684, 1732771130, 1286552655,
                    1917026106, 1619555880]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(INT_MAX))
        self.assertListEqual(actual, expected)


    def test_long_int_max_value(self):
        self.test_int_max_value()


    def test_long_long_max_value(self):
        expected = [5378934912805100368, 1481834513793674581, 2022704902811851265,
                    5525701581272513140, 6955939542478552692, 2825459752566365625,
                    8145320789793645473, 4067308899818932548, 8059721601458305289,
                    1476791508350122857]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(LONG_MAX))
        self.assertListEqual(actual, expected)


    def test_long_32bit(self):
        expected = [2727734613, 1904908801, 3470892473, 360581444, 1854258025,
                    1304656966, 1499749522, 3662865218, 2732253452, 3880916009]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(UINT_MAX))
        self.assertListEqual(actual, expected)


    def test_16(self):
        expected = [5, 14, 7, 9, 8, 2, 14, 4, 13, 5]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(16))
        self.assertListEqual(actual, expected)


    def test_long16(self):
        self.test_16()


    def test_9(self):
        expected = [2, 7, 3, 4, 4, 1, 7, 2, 6, 2]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randrange(9))
        self.assertListEqual(actual, expected)


    def test_next_bytes_asLongArray(self):
        expected = [10757869821655898960, 2963669024859614549, 4045409808013761025,
                    11051403159079484020, 13911879085418020468, 5650919505956806073,
                    16290641578492502945, 8134617799277283652, 16119443202321611017,
                    2953583019140954985]
        actual = []
        for _ in range(10):
            actual.append(self.rand.getrandbits(64))  # bit compatible with nextLong from Java
        self.assertListEqual(actual, expected)


    def test_double(self):
        expected = [0.9206826283274985, 0.6351002019693018, 0.4435211436398484,
                    0.8068844348124993, 0.8926848452848529, 0.8081301250035834,
                    0.25490020128427027, 0.08395441205038512, 0.13853413517651525,
                    0.4317280885585699]
        actual = []
        for _ in range(10):
            actual.append(self.rand.random())
        self.assertListEqual(actual, expected)

    def test_next_bytes(self):
        expected = [0x954ba19aebb1db50, 0x954ba1ebb1db50, 0x954bebb1db50, 0x95ebb1db50]
        bitsizes = [64, 56, 48, 40]
        actual = []
        for bitsize in bitsizes:
            self.rand = RandomPy3k(1234567890)
            actual.append(self.rand.getrandbits(bitsize))  # bit compatible with nextLong from Java
        self.assertListEqual(actual, expected)


    def test_float(self):
        expected = [0.9206826090812683, 0.6351001858711243, 0.4435211420059204, 0.8068844079971313,
                    0.892684817314148, 0.808130145072937, 0.254900187253952, 0.08395440876483917,
                    0.13853412866592407, 0.4317280948162079]
        actual = []
        for _ in range(10):
            # this is what we get if we convert the returned value to float and back
            # this seems to match value in java
            value = struct.unpack("f", struct.pack("f", self.rand.random()))[0]
            actual.append(value)
        self.assertListEqual(actual, expected)


    def test_boolean(self):
        expected = [True, True, True, False, False, False, True, True, True, True]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randbool())
        self.assertListEqual(actual, expected)


if __name__ == "__main__":
    unittest.main()
