from random import Random

class RandomEx(Random):
    def __init__(self, x=None): super().__init__(x)

    def randbool(self): return bool(self.getrandbits(1))