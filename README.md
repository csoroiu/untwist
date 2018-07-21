# untwist
[![Maven Central][maven-tasks-svg]][maven-tasks-link]
[![Javadoc][javadoc-svg]][javadoc-link]
[![Build Status][build-status-svg]][build-status-link]
[![License][license-svg]][license-link]

**untwist** is a collection of PRNG's from **.NET**, **Free Pascal**, **Turbo Pascal**, **Python**.

**Python** and **Free Pascal** use the **Mersenne Twister** as a randomization source.
**Turbo Pascal** and  **Java** are using a **linear congruential generator (LCG)**.
**.NET** is using a modified version of Donald E. Knuth's **subtractive random number generator** algorithm. 

**Given the same input (seed) they generate the same values as the ones in the respective languages.**

Also, besides their normal use, I have added functions to go backward. The **Mersenne Twister** 
and **LCG**'s are reversible.
Also, **LCG**'s and **subtractive generators** are trivial to revert.

I got the idea of reversing the generators from several places, after I had to break the seed of some generator, in order to reduce the size of the archived dataset.

## Disclaimer
*There are some cases in which the reverse does not work as expected. Try not to mix next and prev versions of the methods as you might run into strange situations.*
*Some of the situations are captured by unit tests which are currently marked as ignored.*
*Most of the time things go well.*

***Nevertheless, use it at your own risk. This library comes with no guarantees.***

## Usage
#### Maven dependency
```xml
<dependency>
    <groupId>ro.derbederos</groupId>
    <artifactId>untwist</artifactId>
    <version>0.5</version>
</dependency>
```
#### Gradle dependency
```groovy
compile 'ro.derbederos:untwist:0.5'
```

Enjoy using them!

[build-status-svg]: https://travis-ci.org/csoroiu/untwist.svg?branch=master
[build-status-link]: https://travis-ci.org/csoroiu/untwist
[javadoc-svg]: http://javadoc.io/badge/ro.derbederos/untwist.svg?color=red
[javadoc-link]: http://javadoc.io/doc/ro.derbederos/untwist
[license-svg]: https://img.shields.io/badge/license-Apache2-blue.svg
[license-link]: https://raw.githubusercontent.com/csoroiu/untwist/master/LICENSE
[maven-tasks-svg]: https://img.shields.io/maven-central/v/ro.derbederos/untwist.svg
[maven-tasks-link]: https://maven-badges.herokuapp.com/maven-central/ro.derbederos/untwist
