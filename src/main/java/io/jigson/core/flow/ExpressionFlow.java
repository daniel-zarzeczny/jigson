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
package io.jigson.core.flow;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.jigson.config.Context;
import io.jigson.core.IllegalQueryException;
import io.jigson.core.Token;
import io.jigson.core.TokenizerFactory;
import io.jigson.expression.Operators;
import io.jigson.expression.predicate.Predicate;
import io.jigson.expression.predicate.PredicateFactory;
import io.jigson.pipe.Flow;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a flow responsible for evaluating result of comparison expression,
 * where the left operand is a query but the right operand is a raw numeric value.
 *
 * @author Daniel Zarzeczny
 */
public class ExpressionFlow implements Flow<JsonElement, JsonPrimitive> {

    private static final Set<String> COMPARISON_OPERATORS = Operators.comparisonOperators();
    private static final int INDEX_ONE = 1;

    private final String trimmedQuery;
    private final Context context;

    public ExpressionFlow(final String trimmedQuery, final Context context) {
        this.trimmedQuery = trimmedQuery;
        this.context = context;
    }

    @Override
    public JsonPrimitive flow(final JsonElement jsonElement) {

        final String comparisionOperator = resolveComparisonOperator().orElseThrow(IllegalQueryException::new);

        final int startOperatorIndex = trimmedQuery.lastIndexOf(comparisionOperator);
        final int endOperatorIndex = startOperatorIndex + comparisionOperator.length() - 1;

        final String rightOperandValue = getRightOperand(endOperatorIndex);
        final String leftOperandValue = getLeftOperand(jsonElement, startOperatorIndex);

        final Predicate<String> predicate = PredicateFactory.create(comparisionOperator, leftOperandValue);
        return new JsonPrimitive(predicate.accept(rightOperandValue));
    }

    private Optional<String> resolveComparisonOperator() {

        final String query = trimmedQuery.substring(INDEX_ONE);

        final List<Token> tokens = TokenizerFactory.createQueryTokenizer().tokenize(query);
        return Lists
                .reverse(tokens)
                .stream()
                .map(Token::getToken)
                .filter(COMPARISON_OPERATORS::contains)
                .findFirst();
    }

    private String getRightOperand(final int operatorIndex) {

        final String rightOperandValue = trimmedQuery.substring(operatorIndex + 1).trim();
        if (!StringUtils.isNumeric(rightOperandValue)) {
            throw new IllegalArgumentException("Right operand must be numeric!");
        }
        return rightOperandValue;
    }

    private String getLeftOperand(final JsonElement jsonElement, final int operatorIndex) {
        final String fetchQuery = trimmedQuery.substring(INDEX_ONE, operatorIndex).trim();
        final Query query = Query.from(fetchQuery);
        final String leftOperandValue = new FetchFlow(query, context).flow(jsonElement).getAsString();

        if (!StringUtils.isNumeric(leftOperandValue)) {
            throw new IllegalArgumentException("Left operand must be numeric!");
        }
        return leftOperandValue;
    }
}
