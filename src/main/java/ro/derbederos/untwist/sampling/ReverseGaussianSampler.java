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

import org.apache.commons.rng.sampling.distribution.GaussianSampler;
import ro.derbederos.untwist.ReverseNormalizedGaussianSampler;

public class ReverseGaussianSampler extends GaussianSampler {

    private final ReverseNormalizedGaussianSampler normalized;

    /**
     * @param normalized        Generator of N(0,1) Gaussian distributed random numbers.
     * @param mean              Mean of the Gaussian distribution.
     * @param standardDeviation Standard deviation of the Gaussian distribution.
     */
    public ReverseGaussianSampler(ReverseNormalizedGaussianSampler normalized, double mean, double standardDeviation) {
        super(normalized, mean, standardDeviation);
        this.normalized = normalized;
    }

    public void undoSample() {
        this.normalized.undoSample();
    }
}
