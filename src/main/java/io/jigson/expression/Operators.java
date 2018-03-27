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

import com.google.common.collect.Sets;
import io.jigson.core.Token;
import io.jigson.core.TokenizerFactory;

import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public final class Operators {

    public static final String EQ = "=";
    public static final String NEQ = "!=";

    public static final String LTET = "<=";
    public static final String LT = "<";

    public static final String GTET = ">=";
    public static final String GT = ">";

    public static final String AND = "&&";
    public static final String OR = "||";

    private static final Set<String> COMPARISON_OPERATOR = Sets.newHashSet(EQ, NEQ, LTET, LT, GTET, GT);

    private Operators() {
    }

    public static String findLogicalOperator(final String predicate) {
        return
                TokenizerFactory.createCriteriaTokenizer()
                        .tokenize(predicate)
                        .stream()
                        .filter(t -> t.getIndex() == Token.AND || t.getIndex() == Token.OR)
                        .map(Token::getToken)
                        .findFirst()
                        .orElse(EMPTY);
    }

    public static Set<String> comparisonOperators() {
        return Collections.unmodifiableSet(COMPARISON_OPERATOR);
    }
}
