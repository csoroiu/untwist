/*
 * Copyright (c) 2017 Claudiu Soroiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

public class JDKRandomPy3kCompat extends Random implements RandomGenerator {

    private static final long serialVersionUID = 1L;

    public JDKRandomPy3kCompat() {
        super();
    }

    public JDKRandomPy3kCompat(long seed) {
        super(seed);
    }

    @Override
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomUtils.convertToLong(seed));
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }
}
