package io.jigson.core;

import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class TokenizerTest {

    private static final String SPACE = " ";

    @Test
    public void shouldTokenizeJigsonExpression() {

        // given
        final String expr = "$store(imie!=ADAM).books[].cover(colour=red).image(width<=300) => {\"status\" : \"OK\"}";

        // when
        final List<Token> tokens =
                Tokenizer
                        .newInstance()
                        .withPattern(TokenPattern.DOLLAR_SYMBOL, Token.DOLLAR_SYMBOL)
                        .withPattern(TokenPattern.AT_SYMBOL, Token.AT_SYMBOL)
                        .withPattern(TokenPattern.DOT_SYMBOL, Token.DOT_SYMBOL)
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
                        .withPattern(TokenPattern.OR, Token.OR)
                        .tokenize(expr);

        // then
        final StringBuilder actualExpression = new StringBuilder();
        tokens.stream().map(Token::getToken).forEach(actualExpression::append);

        assertThat(tokens.size()).isEqualTo(35);
        assertThat(actualExpression.toString()).isEqualTo(expr.replace(SPACE, EMPTY));
    }
}
