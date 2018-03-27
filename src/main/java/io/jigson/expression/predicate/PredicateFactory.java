/*
 *    Copyright 2018 the original author or authors
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


import static io.jigson.expression.Operators.*;

/**
 * Represents a factory responsible for creating {@link Predicate}s.
 *
 * @author Daniel Zarzeczny
 */
public final class PredicateFactory {

    private PredicateFactory() {
    }

    public static <T> Predicate<T> create(final String operator, final T leftOperand) {
        if (EQ.equalsIgnoreCase(operator)) {
            return new EqualPredicate<>(leftOperand);
        } else if (GT.equalsIgnoreCase(operator)) {
            return new GreaterThanPredicate<>(leftOperand);
        } else if (LT.equalsIgnoreCase(operator)) {
            return new LessThanPredicate<>(leftOperand);
        } else if (LTET.equalsIgnoreCase(operator)) {
            return new LessThanEqualToPredicate<>(leftOperand);
        } else if (GTET.equalsIgnoreCase(operator)) {
            return new GreaterThanEqualToPredicate<>(leftOperand);
        } else if (NEQ.equalsIgnoreCase(operator)) {
            return new NotEqualPredicate<>(leftOperand);
        }
        throw new IllegalArgumentException("Unknown operator!");
    }
}
