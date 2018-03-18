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

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

public final class Tokenizer {

    private static final String REGEX_PREFIX = "^(";
    private static final String REGEX_SUFFIX = ")";

    private final LinkedList<TokenPattern> patterns = Lists.newLinkedList();
    private final LinkedList<Token> tokens = Lists.newLinkedList();

    private Tokenizer() {
    }

    public static Tokenizer newInstance() {
        return new Tokenizer();
    }

    Tokenizer withPattern(final String regex, final int token) {
        patterns.add(TokenPattern.from(Pattern.compile(REGEX_PREFIX + regex + REGEX_SUFFIX), token));
        return this;
    }

    public List<Token> tokenize(final String expression) {

        String lastExpression = Optional.ofNullable(expression).orElse(EMPTY);
        boolean atLeastOneMatch = true;

        while (isNotBlank(lastExpression) && atLeastOneMatch) {
            atLeastOneMatch = false;
            for (final TokenPattern pattern : patterns) {
                final Matcher matcher = pattern.getRegex().matcher(lastExpression);
                if (matcher.find()) {
                    lastExpression = matcher.replaceFirst(EMPTY).trim();
                    tokens.add(Token.from(pattern.getToken(), matcher.group().trim()));
                    atLeastOneMatch = true;
                    break;
                }
            }
        }
        if (tokens.isEmpty() || isBlank(expression)) {
            throw new UnexpectedSymbolException();
        }
        return tokens;
    }
}
