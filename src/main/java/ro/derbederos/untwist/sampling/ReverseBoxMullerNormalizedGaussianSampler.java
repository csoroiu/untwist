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

import org.apache.commons.rng.sampling.distribution.BoxMullerNormalizedGaussianSampler;
import ro.derbederos.untwist.ReverseNormalizedGaussianSampler;
import ro.derbederos.untwist.ReverseUniformRandomProvider;

public class ReverseBoxMullerNormalizedGaussianSampler extends BoxMullerNormalizedGaussianSampler
        implements ReverseNormalizedGaussianSampler {

    private final ReverseUniformRandomProvider rng;

    /**
     * @param rng Generator of uniformly distributed random numbers.
     */
    public ReverseBoxMullerNormalizedGaussianSampler(ReverseUniformRandomProvider rng) {
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
            rng.prevDouble();
            rng.prevDouble();
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
