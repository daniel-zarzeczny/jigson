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

package io.jigson.config;


import io.jigson.json.filter.JsonArrayFilter;

import java.math.BigDecimal;

import static io.jigson.json.filter.JsonArrayFilter.Strategy.*;

public final class Context {

    private final FilterContext filterContext = new FilterContext();
    private final NumberContext numberContext = new NumberContext();

    private Context() {
    }

    public static Context newContext() {
        return new Context();
    }

    public FilterContext filters() {
        return this.filterContext;
    }

    public NumberContext numbers() {
        return this.numberContext;
    }

    private Context context() {
        return this;
    }

    public class FilterContext {

        private final ArraysContext arraysContext;

        private FilterContext() {
            this.arraysContext = new ArraysContext();
        }

        public ArraysContext arrays() {
            return arraysContext;
        }

        public class ArraysContext {

            private JsonArrayFilter.Strategy filterStrategy;

            private ArraysContext() {
                this.filterStrategy = ALL_IF_ANY_MATCHING;
            }

            public Context allIfAnyMatching() {
                this.filterStrategy = ALL_IF_ANY_MATCHING;
                return context();
            }

            public Context keepMatchingAndPrimitives() {
                this.filterStrategy = KEEP_MATCHING_AND_PRIMITIVES;
                return context();
            }

            public Context onlyMatching() {
                this.filterStrategy = ONLY_MATCHING;
                return context();
            }

            public JsonArrayFilter.Strategy strategy() {
                return this.filterStrategy;
            }
        }
    }

    public class NumberContext {

        private static final int ROUNDING_MODE_AMOUNT = 8;
        private int precision;
        private int roundingMode;

        private NumberContext() {
            this.precision = 2;
            this.roundingMode = BigDecimal.ROUND_HALF_UP;
        }

        public Context withPrecision(final int precision) {
            this.precision = precision;
            return context();
        }

        public NumberContext withPrecisionAnd(final int precision) {
            withPrecision(precision);
            return this;
        }

        public int precision() {
            return Math.abs(precision);
        }

        public Context withRoundingMode(final int roundingMode) {
            this.roundingMode = roundingMode % ROUNDING_MODE_AMOUNT;
            return context();
        }

        public NumberContext roundingModeAnd(final int roundingMode) {
            withRoundingMode(roundingMode);
            return this;
        }

        public int roundingMode() {
            return roundingMode;
        }
    }
}
