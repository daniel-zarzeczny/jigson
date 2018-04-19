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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.jigson.core.JigsonConfigHolder;
import io.jigson.core.Token;
import io.jigson.core.TokenizerFactory;
import io.jigson.expression.ExpressionFactory;
import io.jigson.expression.predicate.Predicate;
import io.jigson.json.filter.JsonArrayFilter;
import io.jigson.json.filter.strategy.JsonFilterStrategyFactory;
import io.jigson.json.pipe.IllegalCriteriaException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class JsonPredicate implements Predicate<JsonElement> {


    private static final int CRITERION_TOKENS_AMOUNT = 3;
    private static final String SPACE = " ";

    private final String criterion;
    private final String propertyName;
    private final String operator;
    private final String propertyValue;

    private JsonPredicate(final String criterion) {
        this.criterion = criterion;
        final List<String> tokens = getTokens();
        this.propertyName = tokens.get(0);
        this.operator = tokens.get(1);
        this.propertyValue = tokens.get(2);
    }

    public static JsonPredicate from(final String criterion) {
        return new JsonPredicate(criterion);
    }

    @Override
    public boolean accept(final JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return accept((JsonPrimitive) jsonElement);
        } else if (jsonElement.isJsonObject()) {
            return accept((JsonObject) jsonElement);
        } else if (jsonElement.isJsonArray()) {
            return accept((JsonArray) jsonElement);
        }
        throw new IllegalArgumentException("JsonElement type not recognized!");
    }

    private Boolean accept(final JsonPrimitive jsonPrimitive) {
        return ExpressionFactory.booleanExpression(jsonPrimitive.getAsString(), operator, propertyValue).interpret();
    }

    private Boolean accept(final JsonObject jsonObject) {
        final String actualValue = jsonObject.getAsJsonPrimitive(propertyName).getAsString();
        return ExpressionFactory.booleanExpression(actualValue, operator, propertyValue).interpret();
    }

    private Boolean accept(final JsonArray jsonArray) {
        final JsonArrayFilter.Strategy filterStrategy = JigsonConfigHolder.get().filters().arrays().strategy();
        final JsonArrayFilter filter = JsonFilterStrategyFactory.createJsonArrayFilter(filterStrategy);
        final JsonElement result = filter.filter(jsonArray, criterion);
        final boolean isArray = result.isJsonArray();
        return isArray && result.getAsJsonArray().size() > 0;
    }

    private List<String> getTokens() {
        final String trimmedCriterion = criterion.trim().replace(SPACE, EMPTY);
        final List<String> tokens =
                TokenizerFactory
                        .createCriteriaTokenizer()
                        .tokenize(trimmedCriterion)
                        .stream()
                        .map(Token::getToken)
                        .collect(toList());
        if (tokens.size() == CRITERION_TOKENS_AMOUNT) {
            return tokens;
        }
        throw new IllegalCriteriaException();
    }
}
