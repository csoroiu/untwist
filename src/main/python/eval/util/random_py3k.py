from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals

from types import MethodType as _MethodType, BuiltinMethodType as _BuiltinMethodType
from warnings import warn as _warn
from random import Random, BPF
import sys

class RandomPy3k(Random):
    def __init__(self, x=None): 
        super(RandomPy3k, self).__init__(x)
        self.randbool = lambda: bool(self.getrandbits(1))
        if (sys.version_info[0] == 2):
            _warn("Python 2.x is being used. Falling back to copy of py3k random implementation.")
            self.randrange = self.__randrange
            self._randbelow = self.__randbelow
            self.choice = self.__choice


    def __randrange(self, start, stop=None, step=1, _int=int):
        # This code is a bit messy to make it fast for the
        # common case while still doing adequate error checking.
        istart = _int(start)
        if istart != start:
            raise ValueError("non-integer arg 1 for randrange()")
        if stop is None:
            if istart > 0:
                return self.__randbelow(istart)
            raise ValueError("empty range for randrange()")

        # stop argument supplied.
        istop = _int(stop)
        if istop != stop:
            raise ValueError("non-integer stop for randrange()")
        width = istop - istart
        if step == 1 and width > 0:
            return istart + self.__randbelow(width)
        if step == 1:
            raise ValueError("empty range for randrange() (%d,%d, %d)" % (istart, istop, width))

        # Non-unit step argument supplied.
        istep = _int(step)
        if istep != step:
            raise ValueError("non-integer step for randrange()")
        if istep > 0:
            n = (width + istep - 1) // istep
        elif istep < 0:
            n = (width + istep + 1) // istep
        else:
            raise ValueError("zero step for randrange()")

        if n <= 0:
            raise ValueError("empty range for randrange()")

        return istart + istep*self.__randbelow(n)


    def __randbelow(self, n, _int=int, maxsize=1<<BPF, _type=type,
               Method=_MethodType, BuiltinMethod=_BuiltinMethodType):
        random = self.random
        getrandbits = self.getrandbits
        if _type(random) is BuiltinMethod or _type(getrandbits) is Method:
            k = n.bit_length()  # don't use (n-1) here because n can be 1
            r = getrandbits(k)          # 0 <= r < 2**k
            while r >= n:
                r = getrandbits(k)
            return r
        # There's an overriden random() method but no new getrandbits() method,
        # so we can only use random() from here.
        if n >= maxsize:
            _warn("Underlying random() generator does not supply \n"
                "enough bits to choose from a population range this large.\n"
                "To remove the range limitation, add a getrandbits() method.")
            return _int(random() * n)
        rem = maxsize % n
        limit = (maxsize - rem) / maxsize   # int(limit * maxsize) % n == 0
        r = random()
        while r >= limit:
            r = random()
        return _int(r*maxsize) % n


    def __choice(self, seq):
        try:
            i = self.__randbelow(len(seq))
        except ValueError:
            raise IndexError('Cannot choose from an empty sequence')
        return seq[i]
