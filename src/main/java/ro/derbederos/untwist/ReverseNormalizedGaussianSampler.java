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

import org.apache.commons.rng.sampling.distribution.NormalizedGaussianSampler;

public interface ReverseNormalizedGaussianSampler extends NormalizedGaussianSampler {

    /**
     * {@inheritDoc}
     */
    @Override
    default double sample() {
        return nextGaussian();
    }

    default void undoSample() {
        undoNextGaussian();
    }

    /**
     * {@inheritDoc}
     */
    double nextGaussian();

    /**
     * This method undoes the effect of {@link #nextGaussian()}. It is not possible to generate
     * the previous gaussian sequence in the same fashion as for the other methods, but we can
     * reverse the effect of calling the {@link #nextGaussian()}.
     */
    void undoNextGaussian();
}
