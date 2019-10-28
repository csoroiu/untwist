/*
 * Copyright (c) 2017-2019 Claudiu Soroiu
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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ro.derbederos.untwist.Utils.between;

/**
 * This class contains the tests from the CoreFx library.
 * <p>
 * <li><a href="https://github.com/dotnet/corefx/blob/master/src/System.Runtime.Extensions/tests/System/Random.cs">
 * .NET CoreFx Random class tests</a></li>
 */
public class DotNetRandomCoreFxTest {

    @Test
    public void unseeded() {
        DotNetRandom r = new DotNetRandom();
        for (int i = 0; i < 1000; i++) {
            int x = r.nextInt(20);
            assertThat(x, between(0, 20));
        }
        for (int i = 0; i < 1000; i++) {
            int x = r.nextInt(20, 30);
            assertThat(x, between(20, 30));
        }
        for (int i = 0; i < 1000; i++) {
            double x = r.nextDouble();
            assertThat(x, between(0.0, 1.0));
        }
    }

    @Test
    public void seeded() {
        int seed = SeedUtils.generateSecureRandomIntSeed();

        DotNetRandom r1 = new DotNetRandom(seed);
        DotNetRandom r2 = new DotNetRandom(seed);

        byte[] b1 = new byte[1000];
        r1.nextBytes(b1);
        byte[] b2 = new byte[1000];
        r2.nextBytes(b2);
        assertThat(b1, equalTo(b2));
        for (int i = 0; i < b1.length; i++) {
            int x1 = r1.nextInt();
            int x2 = r2.nextInt();
            assertThat(x1, equalTo(x2));
        }
    }

    private static int[][] values() {
        return new int[][]{
                {1559595546, 1755192844, 1649316166, 1198642031, 442452829, 1200195957, 1945678308, 949569752, 2099272109, 587775847},
                {534011718, 237820880, 1002897798, 1657007234, 1412011072, 929393559, 760389092, 2026928803, 217468053, 1379662799},
                {1655911537, 867932563, 356479430, 2115372437, 234085668, 658591161, 1722583523, 956804207, 483147644, 24066104},
                {630327709, 1498044246, 1857544709, 426253993, 1203643911, 387788763, 537294307, 2034163258, 748827235, 815953056},
                {1752227528, 2128155929, 1211126341, 884619196, 25718507, 116986365, 1499488738, 964038662, 1014506826, 1607840008},
                {726643700, 610783965, 564707973, 1342984399, 995276750, 1993667614, 314199522, 2041397713, 1280186417, 252243313},
                {1848543519, 1240895648, 2065773252, 1801349602, 1964834993, 1722865216, 1276393953, 971273117, 1545866008, 1044130265},
                {822959691, 1871007331, 1419354884, 112231158, 786909589, 1452062818, 91104737, 2048632168, 1811545599, 1836017217},
                {1944859510, 353635367, 772936516, 570596361, 1756467832, 1181260420, 1053299168, 978507572, 2077225190, 480420522},
                {919275682, 983747050, 126518148, 1028961564, 578542428, 910458022, 2015493599, 2055866623, 195421134, 1272307474},
                {2041175501, 1613858733, 1627583427, 1487326767, 1548100671, 639655624, 830204383, 985742027, 461100725, 2064194426},
                {1015591673, 96486769, 981165059, 1945691970, 370175267, 368853226, 1792398814, 2063101078, 726780316, 708597731},
                {2137491492, 726598452, 334746691, 256573526, 1339733510, 98050828, 607109598, 992976482, 992459907, 1500484683},
                {1111907664, 1356710135, 1835811970, 714938729, 161808106, 1974732077, 1569304029, 2070335533, 1258139498, 144887988},
                {86323836, 1986821818, 1189393602, 1173303932, 1131366349, 1703929679, 384014813, 1000210937, 1523819089, 936774940},
                {1208223655, 469449854, 542975234, 1631669135, 2100924592, 1433127281, 1346209244, 2077569988, 1789498680, 1728661892},
                {182639827, 1099561537, 2044040513, 2090034338, 922999188, 1162324883, 160920028, 1007445392, 2055178271, 373065197},
                {1304539646, 1729673220, 1397622145, 400915894, 1892557431, 891522485, 1123114459, 2084804443, 173374215, 1164952149},
                {278955818, 212301256, 751203777, 859281097, 714632027, 620720087, 2085308890, 1014679847, 439053806, 1956839101},
                {1400855637, 842412939, 104785409, 1317646300, 1684190270, 349917689, 900019674, 2092038898, 704733397, 601242406},
        };
    }

    @Test
    public void expectedValues() {
        int[][] expectedValues = values();
        for (int seed = 0; seed < expectedValues.length; seed++) {
            var r = new DotNetRandom(seed);
            for (int i = 0; i < expectedValues[seed].length; i++) {
                assertThat(r.nextInt(), equalTo(expectedValues[seed][i]));
            }
        }
    }

    private static byte[][] byteValues() {
        return new byte[][]{
                {(byte) 0x1A, (byte) 0xC, (byte) 0x46, (byte) 0x6F, (byte) 0x5D, (byte) 0x75, (byte) 0xE4, (byte) 0xD8, (byte) 0xAD, (byte) 0x67},
                {(byte) 0x46, (byte) 0xD0, (byte) 0x86, (byte) 0x82, (byte) 0x40, (byte) 0x97, (byte) 0xE4, (byte) 0xA3, (byte) 0x95, (byte) 0xCF},
                {(byte) 0x71, (byte) 0x93, (byte) 0xC6, (byte) 0x95, (byte) 0x24, (byte) 0xB9, (byte) 0xE3, (byte) 0x6F, (byte) 0x7C, (byte) 0x38},
                {(byte) 0x9D, (byte) 0x56, (byte) 0x5, (byte) 0xA9, (byte) 0x7, (byte) 0xDB, (byte) 0xE3, (byte) 0x3A, (byte) 0x63, (byte) 0xA0},
                {(byte) 0xC8, (byte) 0x19, (byte) 0x45, (byte) 0xBC, (byte) 0xEB, (byte) 0xFD, (byte) 0xE2, (byte) 0x6, (byte) 0x4A, (byte) 0x8},
                {(byte) 0xF4, (byte) 0xDD, (byte) 0x85, (byte) 0xCF, (byte) 0xCE, (byte) 0x1E, (byte) 0xE2, (byte) 0xD1, (byte) 0x31, (byte) 0x71},
                {(byte) 0x1F, (byte) 0xA0, (byte) 0xC4, (byte) 0xE2, (byte) 0xB1, (byte) 0x40, (byte) 0xE1, (byte) 0x9D, (byte) 0x18, (byte) 0xD9},
                {(byte) 0x4B, (byte) 0x63, (byte) 0x4, (byte) 0xF6, (byte) 0x95, (byte) 0x62, (byte) 0xE1, (byte) 0x68, (byte) 0xFF, (byte) 0x41},
                {(byte) 0x76, (byte) 0x27, (byte) 0x44, (byte) 0x9, (byte) 0x78, (byte) 0x84, (byte) 0xE0, (byte) 0x34, (byte) 0xE6, (byte) 0xAA},
                {(byte) 0xA2, (byte) 0xEA, (byte) 0x84, (byte) 0x1C, (byte) 0x5C, (byte) 0xA6, (byte) 0xDF, (byte) 0xFF, (byte) 0xCE, (byte) 0x12},
                {(byte) 0xCD, (byte) 0xAD, (byte) 0xC3, (byte) 0x2F, (byte) 0x3F, (byte) 0xC8, (byte) 0xDF, (byte) 0xCB, (byte) 0xB5, (byte) 0x7A},
                {(byte) 0xF9, (byte) 0x71, (byte) 0x3, (byte) 0x42, (byte) 0x23, (byte) 0xEA, (byte) 0xDE, (byte) 0x96, (byte) 0x9C, (byte) 0xE3},
                {(byte) 0x24, (byte) 0x34, (byte) 0x43, (byte) 0x56, (byte) 0x6, (byte) 0xC, (byte) 0xDE, (byte) 0x62, (byte) 0x83, (byte) 0x4B},
                {(byte) 0x50, (byte) 0xF7, (byte) 0x82, (byte) 0x69, (byte) 0xEA, (byte) 0x2D, (byte) 0xDD, (byte) 0x2D, (byte) 0x6A, (byte) 0xB4},
                {(byte) 0x7C, (byte) 0xBA, (byte) 0xC2, (byte) 0x7C, (byte) 0xCD, (byte) 0x4F, (byte) 0xDD, (byte) 0xF9, (byte) 0x51, (byte) 0x1C},
                {(byte) 0xA7, (byte) 0x7E, (byte) 0x2, (byte) 0x8F, (byte) 0xB0, (byte) 0x71, (byte) 0xDC, (byte) 0xC4, (byte) 0x38, (byte) 0x84},
                {(byte) 0xD3, (byte) 0x41, (byte) 0x41, (byte) 0xA2, (byte) 0x94, (byte) 0x93, (byte) 0xDC, (byte) 0x90, (byte) 0x1F, (byte) 0xED},
                {(byte) 0xFE, (byte) 0x4, (byte) 0x81, (byte) 0xB6, (byte) 0x77, (byte) 0xB5, (byte) 0xDB, (byte) 0x5B, (byte) 0x7, (byte) 0x55},
                {(byte) 0x2A, (byte) 0xC8, (byte) 0xC1, (byte) 0xC9, (byte) 0x5B, (byte) 0xD7, (byte) 0xDA, (byte) 0x27, (byte) 0xEE, (byte) 0xBD},
                {(byte) 0x55, (byte) 0x8B, (byte) 0x1, (byte) 0xDC, (byte) 0x3E, (byte) 0xF9, (byte) 0xDA, (byte) 0xF2, (byte) 0xD5, (byte) 0x26}
        };
    }

    @Test
    public void expectedValues_NextBytesArray() {
        byte[][] expectedValues = byteValues();
        for (int seed = 0; seed < expectedValues.length; seed++) {
            byte[] actualValues = new byte[expectedValues[seed].length];
            var r = new DotNetRandom(seed);
            r.nextBytes(actualValues);
            assertThat(actualValues, equalTo(expectedValues[seed]));
        }
    }

    @Test
    public void Sample() {
        SubRandom r = new SubRandom();

        for (int i = 0; i < 1000; i++) {
            double d = r.exposeSample();
            assertThat(d, between(0.0, 1.0));
        }
    }

    static class SubRandom extends DotNetRandom {
        double exposeSample() {
            return sample();
        }
    }
}
