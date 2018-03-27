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

public final class TokenizerFactory {

    private TokenizerFactory() {
    }

    public static Tokenizer createCriteriaTokenizer() {
        return Tokenizer
                .newInstance()
                .withPattern(TokenPattern.VARIABLE, Token.VARIABLE)
                .withPattern(TokenPattern.NUMBER, Token.NUMBER)
                .withPattern(TokenPattern.EQ, Token.EQ)
                .withPattern(TokenPattern.NEQ, Token.NEQ)
                .withPattern(TokenPattern.GTET, Token.GTET)
                .withPattern(TokenPattern.GT, Token.GT)
                .withPattern(TokenPattern.LTET, Token.LTET)
                .withPattern(TokenPattern.LT, Token.LT)
                .withPattern(TokenPattern.AND, Token.AND)
                .withPattern(TokenPattern.OR, Token.OR);
    }

    public static Tokenizer createQueryTokenizer() {
        return Tokenizer
                .newInstance()
                .withPattern(TokenPattern.DOLLAR_SYMBOL, Token.DOLLAR_SYMBOL)
                .withPattern(TokenPattern.AT_SYMBOL, Token.AT_SYMBOL)
                .withPattern(TokenPattern.DOT_SYMBOL, Token.DOT_SYMBOL)
                .withPattern(TokenPattern.QUESTION_MARK, Token.QUESTION_MARK)
                .withPattern(TokenPattern.COLON, Token.COLON)
                .withPattern(TokenPattern.QUOTE, Token.QUOTE)
                .withPattern(TokenPattern.VARIABLE, Token.VARIABLE)
                .withPattern(TokenPattern.NUMBER, Token.NUMBER)
                .withPattern(TokenPattern.OPEN_BRACKET, Token.OPEN_BRACKET)
                .withPattern(TokenPattern.CLOSE_BRACKET, Token.CLOSE_BRACKET)
                .withPattern(TokenPattern.OPEN_SQ_BRACKET, Token.OPEN_SQ_BRACKET)
                .withPattern(TokenPattern.CLOSE_SQ_BRACKET, Token.CLOSE_SQ_BRACKET)
                .withPattern(TokenPattern.OPEN_CURLY_BRACE, Token.OPEN_CURLY_BRACE)
                .withPattern(TokenPattern.CLOSE_CURLY_BRACE, Token.CLOSE_CURLY_BRACE)
                .withPattern(TokenPattern.THEN, Token.THEN)
                .withPattern(TokenPattern.EQ, Token.EQ)
                .withPattern(TokenPattern.NEQ, Token.NEQ)
                .withPattern(TokenPattern.GTET, Token.GTET)
                .withPattern(TokenPattern.GT, Token.GT)
                .withPattern(TokenPattern.LTET, Token.LTET)
                .withPattern(TokenPattern.LT, Token.LT)
                .withPattern(TokenPattern.AND, Token.AND)
                .withPattern(TokenPattern.OR, Token.OR);
    }

    public static Tokenizer createSliceTokenizer() {
        return Tokenizer
                .newInstance()
                .withPattern(TokenPattern.NUMBER, Token.NUMBER)
                .withPattern(TokenPattern.COLON, Token.COLON);
    }

    public static Tokenizer createComparisonTokenizer() {
        return Tokenizer
                .newInstance()
                .withPattern(TokenPattern.EQ, Token.EQ)
                .withPattern(TokenPattern.NEQ, Token.NEQ)
                .withPattern(TokenPattern.GTET, Token.GTET)
                .withPattern(TokenPattern.GT, Token.GT)
                .withPattern(TokenPattern.LTET, Token.LTET)
                .withPattern(TokenPattern.LT, Token.LT);
    }
}
