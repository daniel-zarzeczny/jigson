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

package io.jigson.json.expression;

import com.google.gson.JsonElement;
import io.jigson.expression.ExpressionFactory;
import io.jigson.expression.SimpleExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static io.jigson.expression.Operators.*;
import static org.apache.commons.lang3.StringUtils.*;

public class JsonExpression implements SimpleExpression {

    private static final String SPACE = " ";

    private final JsonElement jsonElement;
    private final String criterion;

    JsonExpression(final JsonElement jsonElement, final String criterion) {
        this.jsonElement = jsonElement;
        this.criterion = criterion.trim().replace(SPACE, EMPTY);
    }

    public static JsonExpression from(final JsonElement jsonElement, final String criterion) {
        return new JsonExpression(jsonElement, criterion);
    }

    @Override
    public Boolean interpret() {
        return isBlank(criterion) || createExpression(jsonElement).interpret();
    }

    private JsonExpression createExpression(final JsonElement jsonElement) {

        final String logicalOperator = findLogicalOperator(criterion);

        if (isNotBlank(logicalOperator)) {
            final boolean initialState = resolveInitialState(logicalOperator);

            final List<String> criteria = splitCriterionIfComplex(logicalOperator);
            final Boolean result =
                    criteria
                            .stream()
                            .map(c -> createPredicate(c).accept(jsonElement))
                            .reduce(initialState, (a, b) -> interpret(a, logicalOperator, b));
            return JsonExpressionFactory.from(result);
        }
        final boolean isAccepted = JsonPredicate.from(criterion).accept(jsonElement);
        return JsonExpressionFactory.from(isAccepted);
    }

    private JsonPredicate createPredicate(final String criterion) {
        return JsonPredicate.from(criterion);
    }

    private List<String> splitCriterionIfComplex(final String logicalOperator) {
        return Arrays.asList(StringUtils.split(criterion, logicalOperator));
    }

    private boolean resolveInitialState(final String logicalOperator) {
        if (AND.equals(logicalOperator)) {
            return Boolean.TRUE;
        } else if (OR.equals(logicalOperator)) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Couldn't not recognize logical operator!");
    }

    private Boolean interpret(final Boolean left, final String logicalOperator, final Boolean right) {
        final JsonExpression leftExpression = JsonExpressionFactory.from(left);
        final JsonExpression rightExpression = JsonExpressionFactory.from(right);
        return ExpressionFactory.booleanExpression(leftExpression, logicalOperator, rightExpression).interpret();
    }
}
