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

import org.junit.Ignore;

import java.util.Random;

@Ignore
class RandomAdaptor extends Random {
    private static final long serialVersionUID = 1L;

    private final ReverseUniformRandomProvider randomProvider;

    RandomAdaptor(ReverseUniformRandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    @Override
    public void nextBytes(byte[] bytes) {
        randomProvider.nextBytes(bytes);
    }

    @Override
    public int nextInt() {
        return randomProvider.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return randomProvider.nextInt(n);
    }

    @Override
    public long nextLong() {
        return randomProvider.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return randomProvider.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return randomProvider.nextFloat();
    }

    @Override
    public double nextDouble() {
        return randomProvider.nextDouble();
    }

    @Override
    protected int next(int bits) {
        return super.next(bits);
    }

    @Override
    public void setSeed(long seed) {
        if (randomProvider != null) { //work around miserable override NPE in constructor.
            throw new UnsupportedOperationException();
        }
    }
}
