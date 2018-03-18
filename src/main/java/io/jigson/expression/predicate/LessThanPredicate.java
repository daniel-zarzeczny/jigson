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

package io.jigson.expression.predicate;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isNumeric;


public class LessThanPredicate<T> implements Predicate<T> {

    private final T left;

    public LessThanPredicate(final T left) {
        this.left = left;
    }

    @Override
    public boolean accept(final T right) {

        final String leftValue = String.valueOf(left);
        final String rightValue = String.valueOf(right);

        if (isNumeric(leftValue) && isNumeric(rightValue)) {
            final BigDecimal one = new BigDecimal(leftValue);
            final BigDecimal two = new BigDecimal(rightValue);
            return one.compareTo(two) < 0;
        }
        throw new IllegalArgumentException("Not a number!");
    }
}
