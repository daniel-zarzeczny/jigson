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


import java.io.Serializable;

public class Token implements Serializable {

    public static final int DOLLAR_SYMBOL = 1;
    public static final int AT_SYMBOL = 2;
    public static final int QUESTION_MARK = 3;
    public static final int HASH_SYMBOL = 4;
    public static final int DOT_SYMBOL = 5;
    public static final int QUOTE = 6;
    public static final int COLON = 7;
    public static final int VARIABLE = 8;
    public static final int NUMBER = 9;
    public static final int OPEN_BRACKET = 10;
    public static final int CLOSE_BRACKET = 11;
    public static final int OPEN_SQ_BRACKET = 12;
    public static final int CLOSE_SQ_BRACKET = 13;
    public static final int OPEN_CURLY_BRACE = 14;
    public static final int CLOSE_CURLY_BRACE = 15;

    public static final int EQ = 16;
    public static final int NEQ = 17;
    public static final int LTET = 18;
    public static final int LT = 19;
    public static final int GTET = 20;
    public static final int GT = 21;
    public static final int AND = 22;
    public static final int OR = 23;
    public static final int THEN = 24;

    private final int index;
    private final String token;

    private Token(final int index, final String token) {
        this.index = index;
        this.token = token;
    }

    public static Token from(final int index, final String token) {
        return new Token(index, token);
    }

    public int getIndex() {
        return index;
    }

    public String getToken() {
        return token;
    }
}
