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

package io.jigson.expression;

import static io.jigson.expression.Operators.*;

public final class ExpressionFactory {

    private ExpressionFactory() {
    }

    public static BooleanExpression booleanExpression(final Object left, final String operator, final Object right) {
        if (EQ.equals(operator)) {
            return new EqualExpression((String) left, (String) right);
        } else if (NEQ.equals(operator)) {
            return new NotEqualExpression((String) left, (String) right);
        } else if (GTET.equals(operator)) {
            return new GreaterThanEqualToExpression((String) left, (String) right);
        } else if (GT.equals(operator)) {
            return new GreaterThanExpression((String) left, (String) right);
        } else if (LTET.equals(operator)) {
            return new LessThanEqualToExpression((String) left, (String) right);
        } else if (LT.equals(operator)) {
            return new LessThanExpression((String) left, (String) right);
        } else if (AND.equals(operator)) {
            return new AndExpression((SimpleExpression) left, (SimpleExpression) right);
        } else if (OR.equals(operator)) {
            return new OrExpression((SimpleExpression) left, (SimpleExpression) right);
        }
        throw new IllegalArgumentException("Unsupported operator: " + operator + "!");
    }
}
