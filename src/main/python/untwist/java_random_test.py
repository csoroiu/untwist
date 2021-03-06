from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals

import unittest

from untwist.java_random_35 import JavaRandom


class JavaRandomTest(unittest.TestCase):
    def setUp(self):
        self.r = JavaRandom(0)

    def test_next_bytes(self):
        standard = [96, -76, 32, -69, 56, 81, -39, -44]
        bytes_count = [0] * len(standard)
        self.r.next_bytes(bytes_count)
        self.assertEqual(bytes_count, standard)

    def test_next_int(self):
        standard = -1155484576
        self.assertEqual(self.r.next_int(), standard)

    def test_next_int_lue(self):
        standard = 12
        self.assertEqual(self.r.next_int(42), standard)

    def test_randrange_1param(self):
        standard = 12
        self.assertEqual(self.r.randrange(42), standard)

    def test_randrange_2params(self):
        standard = 14
        self.assertEqual(self.r.randrange(2, 44), standard)

    def test_randint_2params(self):
        standard = 14
        self.assertEqual(self.r.randint(2, 43), standard)

    def test_get_seed(self):
        actual = 42523532
        rnd = JavaRandom(42523532)
        self.assertEqual(rnd.get_seed(), actual)

    def test_set_seed(self):
        self.r.set_seed(42523532)
        expected = -1778905166
        self.assertEqual(self.r.next_int(), expected)

    def test_next_int_pow_2(self):
        actual = self.r.next_int(2 ** 30)
        self.assertEqual(actual, 784870680)

    def test_next_int_int_overflow(self):
        self.r.set_seed(215660466117472)
        standard = 4224
        self.assertEqual(self.r.next_int(100000), standard)

    def test_next_long(self):
        standard = -4962768465676381896
        self.assertEqual(self.r.next_long(), standard)

    def test_next_boolean(self):
        standard = True
        self.assertEqual(self.r.next_boolean(), standard)

    def test_next_float(self):
        # in java 0.73096776f = 0.7309677600860596d, same value we get
        # in python when we work with double precision
        standard = float(0.7309677600860596)
        self.assertEqual(self.r.next_float(), standard)

    def test_next_double(self):
        standard = 0.73096778737665700
        self.assertEqual(self.r.next_double(), standard)

    def test_random(self):
        standard = 0.73096778737665700
        self.assertEqual(self.r.random(), standard)


# def test_nextGaussian(self):
#         standard = 0.80253306373903050, -0.90154608841751220
#         self.assertEqual((self.r.nextGaussian(), self.r.nextGaussian()),
#             standard)

if __name__ == "__main__":
    unittest.main()
