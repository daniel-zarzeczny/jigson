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

import java.util.regex.Pattern;

final class TokenPattern {

    public static final String DOLLAR_SYMBOL = "\\$";
    public static final String AT_SYMBOL = "\\@";
    public static final String DOT_SYMBOL = "\\.";
    public static final String QUESTION_MARK = "\\?";
    public static final String QUOTE = "\\\"";
    public static final String COLON = "\\:";
    public static final String VARIABLE = "[a-zA-Z][a-zA-Z0-9_]*";
    public static final String NUMBER = "\\d+";
    public static final String OPEN_BRACKET = "\\(";
    public static final String CLOSE_BRACKET = "\\)";
    public static final String OPEN_SQ_BRACKET = "\\[";
    public static final String CLOSE_SQ_BRACKET = "\\]";
    public static final String OPEN_CURLY_BRACE = "\\{";
    public static final String CLOSE_CURLY_BRACE = "\\}";

    public static final String EQ = "\\=";
    public static final String NEQ = "\\!=";
    public static final String LTET = "\\<=";
    public static final String LT = "\\<";
    public static final String GTET = "\\>=";
    public static final String GT = "\\>";
    public static final String AND = "\\&&";
    public static final String OR = "\\|\\|";
    public static final String THEN = "\\=>";

    private final Pattern regex;
    private final int token;

    private TokenPattern(final Pattern regex, final int token) {
        this.regex = regex;
        this.token = token;
    }

    public static TokenPattern from(final Pattern regex, final int token) {
        return new TokenPattern(regex, token);
    }

    public Pattern getRegex() {
        return regex;
    }

    public int getToken() {
        return token;
    }
}
