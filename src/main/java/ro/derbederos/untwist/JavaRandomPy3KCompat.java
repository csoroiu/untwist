/*
 * Copyright (c) 2017-2018 Claudiu Soroiu
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

public class JavaRandomPy3KCompat extends ReversibleJavaRandom {

    private static final long serialVersionUID = 1L;

    public JavaRandomPy3KCompat() {
        super();
    }

    public JavaRandomPy3KCompat(long seed) {
        super(seed);
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override
    public float prevFloat() {
        return (float) prevDouble();
    }
}
