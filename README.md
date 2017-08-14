# untwist
[![Maven Central][maven-tasks-svg]][maven-tasks-link]
[![Javadoc][javadoc-svg]][javadoc-link]
[![Build Status][build-status-svg]][build-status-link]
[![License][license-svg]][license-link]

**untwist** is a collection of PRNG's from **.NET**, **Free Pascal**, **Turbo Pascal**, **Python**.

**Python** and **Free Pascal** use the **Mersenne Twister** as a randomization source.
**Turbo Pascal** and  **Java** are using a **linear congruential generator (LCG)**.
**CLR (.NET)** is using a modified version of Donald E. Knuth's **subtractive random number generator** algorithm. 

**Given the same input (seed) they generate the same values as the ones in the respective languages.**

Also, besides their normal use, I have added functions to go backward. The **Mersenne Twister** 
and **LCG**'s are reversible.
I got the idea of reversing the MT from here: [Cracking Random Number Generators - Part 3](https://jazzy.id.au/2010/09/22/cracking_random_number_generators_part_3.html).
Also, **LCG**'s and **subtractive generators** are trivial to revert.

Enjoy using them!

[build-status-svg]: https://travis-ci.org/csoroiu/untwist.svg?branch=master
[build-status-link]: https://travis-ci.org/csoroiu/untwist
[javadoc-svg]: http://javadoc.io/badge/ro.derbederos/untwist.svg?color=red
[javadoc-link]: http://javadoc.io/doc/ro.derbederos/untwist
[license-svg]: https://img.shields.io/badge/license-Apache2-blue.svg
[license-link]: https://raw.githubusercontent.com/csoroiu/untwist/master/LICENSE
[maven-tasks-svg]: https://img.shields.io/maven-central/v/ro.derbederos/untwist.svg
[maven-tasks-link]: https://maven-badges.herokuapp.com/maven-central/ro.derbederos/untwist
