from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals


def get_permutation(n, generator, steps=1):
    perm = []
    for i in range(n):
        perm.append(i)
    for _ in range(steps):
        # http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        for i in range(n):
            j = generator.next_int(n)
            if i == j:
                continue
            perm[i] ^= perm[j]
            perm[j] ^= perm[i]
            perm[i] ^= perm[j]
    return perm


def get_permutation_inside_out(n, generator):
    # http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    perm = [0]
    for i in range(1, n):
        j = generator.next_int(i + 1)
        perm.append(perm[j])
        perm[j] = i
    return perm
