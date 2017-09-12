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

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.RandomUtils.nextDoubles;
import static ro.derbederos.untwist.Utils.nextFloats;

public class TurboPascalRandomCoprocEnabledTest extends TurboPascalRandomTest {

    @Override
    protected TurboPascalRandom makeGenerator() {
        return new TurboPascalRandom(0xC44002DC, true);
    }

    @Override
    @Test
    public void testZero() {
        //-1498392781 seed was causing the pascal generator to generate 1.0 in turbo pascal 5 and 6.
        TurboPascalRandom generator = new TurboPascalRandom(-1498392781, true);
        assertThat(generator.nextDouble(), equalTo(0.0));
    }

    @Override
    @Test
    public void testNextDoubleExactValue() {
        double[] expected = {0.30312337283976376, 0.013781279791146517, 0.18803224223665893, 0.31765864603221416,
                0.27547088847495615, 0.9520445461384952, 0.718031702330336, 0.44134502578526735,
                0.6637153711635619, 0.7491658166982234};
        double[] actual = nextDoubles(generator, expected.length).toArray();

        assertThat(actual, equalTo(expected));
    }

    @Override
    @Test
    public void testNextFloatExactValue() {
        float[] expected = {0.30312338F, 0.013781279F, 0.18803224F, 0.31765863F, 0.27547088F,
                0.95204455F, 0.7180317F, 0.44134504F, 0.66371536F, 0.74916583F};
        float[] actual = nextFloats(generator, expected.length);

        assertThat(actual, equalTo(expected));
    }

    @Test
    @Ignore
    public void testEnabledVsDisabled() {
        TurboPascalRandom r1 = new TurboPascalRandom(1);
        TurboPascalRandom r2 = new TurboPascalRandom(1, true);
        Matcher<Double> equalToMatcher = equalTo(0.5d);
        for (long i = 0; i < 1L << 32; i++) {
            assertThat(Math.abs(r1.nextDouble() - r2.nextDouble()), equalToMatcher);
        }
    }
}
