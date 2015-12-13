from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals
from eval.util.random_py3k import RandomPy3k

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


    def test_double(self):
        expected = [0.9206826283274985, 0.6351002019693018, 0.4435211436398484,
                    0.8068844348124993, 0.8926848452848529, 0.8081301250035834,
                    0.25490020128427027, 0.08395441205038512, 0.13853413517651525,
                    0.4317280885585699]
        actual = []
        for _ in range(10):
            actual.append(self.rand.random())
        self.assertListEqual(actual, expected)


#     def test_float(self):
#         expected = [0.9206826, 0.6351002, 0.44352114, 0.8068844,
#                     0.8926848, 0.80813015, 0.2549002, 0.08395441,
#                     0.13853413, 0.4317281]
#         actual = []
#         for _ in range(10):
#             actual.append(self.rand.random())
#         self.assertListEqual(actual, expected)


    def test_boolean(self):
        expected = [True, True, True, False, False, False, True, True, True, True]
        actual = []
        for _ in range(10):
            actual.append(self.rand.randbool())
        self.assertListEqual(actual, expected)


if __name__ == "__main__":
    unittest.main()
