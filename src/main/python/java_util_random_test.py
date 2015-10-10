from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals
import unittest

from java_util_random import JavaRandom


class JavaRandomTest(unittest.TestCase):

    def setUp(self):
        self.r = JavaRandom(0)


    def test_trivial(self):
        pass


    def test_nextBytes(self):
        standard = [96, -76, 32, -69, 56, 81, -39, -44]
        l = [None] * len(standard)
        self.r.next_bytes(l)
        self.assertEqual(l, standard)


    def test_nextInt(self):
        standard = -1155484576
        self.assertEqual(self.r.next_int(), standard)


    def test_nextInt_lue(self):
        standard = 12
        self.assertEqual(self.r.next_int(42), standard)


    def test_get_seed(self):
        actual = 42523532
        rnd = JavaRandom(42523532)
        self.assertEqual(rnd.seed, actual)


    def test_set_seed(self):
        actual = 42523532
        rnd = JavaRandom()
        rnd.set_seed(42523532)
        self.assertEqual(rnd.seed, actual)


    def test_next_int_pow_2(self):
        actual = self.r.next_int(2 ** 30)
        self.assertEqual(actual, 784870680)


    def test_nextInt_int_overflow(self):
        rnd = JavaRandom(215660466117472)
        standard = 4224
        self.assertEqual(rnd.next_int(100000), standard)


    def test_nextLong(self):
        standard = -4962768465676381896
        self.assertEqual(self.r.next_long(), standard)


    def test_nextBoolean(self):
        standard = True
        self.assertEqual(self.r.next_boolean(), standard)


    def test_nextFloat(self):
        standard = 0.73096776008605960
        self.assertEqual(self.r.next_float(), standard)


    def test_nextDouble(self):
        standard = 0.73096778737665700
        self.assertEqual(self.r.next_double(), standard)


#     def test_nextGaussian(self):
#         standard = 0.80253306373903050, -0.90154608841751220
#         self.assertEqual((self.r.nextGaussian(), self.r.nextGaussian()),
#             standard)

if __name__ == "__main__":
    unittest.main()