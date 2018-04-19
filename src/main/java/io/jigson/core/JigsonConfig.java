/*
 *    Copyright 2018 Daniel Zarzeczny
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.jigson.core;


import io.jigson.json.filter.JsonArrayFilter;

import java.math.BigDecimal;

import static io.jigson.json.filter.JsonArrayFilter.Strategy.*;

public final class JigsonConfig {


    private final FiltersConfig filtersConfig = new FiltersConfig();
    private final NumbersConfig numbersConfig = new NumbersConfig();

    private JigsonConfig() {
    }

    public static JigsonConfig newInstance() {
        return new JigsonConfig();
    }

    public FiltersConfig filters() {
        return this.filtersConfig;
    }

    public NumbersConfig numbers() {
        return this.numbersConfig;
    }

    private JigsonConfig config() {
        return this;
    }

    public class FiltersConfig {

        private final ArraysConfig arraysConfig;

        private FiltersConfig() {
            this.arraysConfig = new ArraysConfig();
        }

        public ArraysConfig arrays() {
            return arraysConfig;
        }

        public class ArraysConfig {

            private JsonArrayFilter.Strategy filterStrategy;

            private ArraysConfig() {
                this.filterStrategy = ALL_IF_ANY_MATCHING;
            }

            public JigsonConfig allIfAnyMatching() {
                this.filterStrategy = ALL_IF_ANY_MATCHING;
                return config();
            }

            public JigsonConfig keepMatchingAndPrimitives() {
                this.filterStrategy = KEEP_MATCHING_AND_PRIMITIVES;
                return config();
            }

            public JigsonConfig onlyMatching() {
                this.filterStrategy = ONLY_MATCHING;
                return config();
            }

            public JsonArrayFilter.Strategy strategy() {
                return this.filterStrategy;
            }
        }
    }

    public class NumbersConfig {

        private static final int ROUNDING_MODE_AMOUNT = 8;
        private int precision;
        private int roundingMode;

        private NumbersConfig() {
            this.precision = 2;
            this.roundingMode = BigDecimal.ROUND_HALF_UP;
        }

        public JigsonConfig withPrecision(final int precision) {
            this.precision = precision;
            return config();
        }

        public NumbersConfig withPrecisionAnd(final int precision) {
            withPrecision(precision);
            return this;
        }

        public int precision() {
            return Math.abs(precision);
        }

        public JigsonConfig withRoundingMode(final int roundingMode) {
            this.roundingMode = roundingMode % ROUNDING_MODE_AMOUNT;
            return config();
        }

        public NumbersConfig roundingModeAnd(final int roundingMode) {
            withRoundingMode(roundingMode);
            return this;
        }

        public int roundingMode() {
            return roundingMode;
        }
    }


}
