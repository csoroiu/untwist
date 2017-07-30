# untwist
[![License][license-svg]][license-link]

**untwist** is a collection of PRNG's collected from CLR (.NET), FreePascal, TurboPascal, Python.

**Python** and **FreePascal** use the **Mersenne Twister** as a randomization source.
**TurboPascal**, like **Java** is using a linear LCG generator.
**CLR (.NET)** is using a modified version of Donald E. Knuth's subtractive random number generator algorithm. 

Given the same input (seed) they generate the same values as the ones in the respective languages.

Also, besides their normal use, I have added functions to go backward. The **Mersenne Twister** 
and **LCG**'s are reversible.
I got the idea of reversing the MT from here: [Cracking Random Number Generators - Part 3](https://jazzy.id.au/2010/09/22/cracking_random_number_generators_part_3.html).
Also, LCG's are trivial to revert.

Enjoy using them!

[license-svg]: https://img.shields.io/badge/license-Apache2-blue.svg
[license-link]: https://raw.githubusercontent.com/csoroiu/untwist/master/LICENSE
