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

package ro.derbederos.untwist.sampling;

import org.apache.commons.rng.sampling.distribution.MarsagliaNormalizedGaussianSampler;
import ro.derbederos.untwist.ReverseNormalizedGaussianSampler;
import ro.derbederos.untwist.ReverseUniformRandomProvider;

public class ReverseMarsagliaNormalizedGaussianSampler extends MarsagliaNormalizedGaussianSampler
        implements ReverseNormalizedGaussianSampler {

    private final ReverseUniformRandomProvider rng;

    /**
     * @param rng Generator of uniformly distributed random numbers.
     */
    public ReverseMarsagliaNormalizedGaussianSampler(ReverseUniformRandomProvider rng) {
        super(rng);
        this.rng = rng;
    }


    @Override
    public double nextGaussian() {
        shouldReverseGaussian = !shouldReverseGaussian;
        hasNextGaussian = !hasNextGaussian;
        return super.sample();
    }

    private boolean shouldReverseGaussian;
    private boolean hasNextGaussian;

    public void undoNextGaussian() {
        if (shouldReverseGaussian) {
            double v1, v2, s;
            do {
                v1 = 2 * rng.prevDouble() - 1; // between -1 and 1
                v2 = 2 * rng.prevDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            clear();
        } else {
            shouldReverseGaussian = true;
        }
    }

    private void clear() {
        shouldReverseGaussian = false;
        if (hasNextGaussian) {
            super.sample();
            hasNextGaussian = false;
        }
    }
}
