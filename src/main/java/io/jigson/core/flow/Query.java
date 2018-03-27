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

package io.jigson.core.flow;

import com.google.common.collect.Lists;
import io.jigson.core.Token;
import io.jigson.core.TokenizerFactory;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class Query implements Iterator<Token> {

    private final List<Token> tokens;
    private final ListIterator<Token> iterator;

    private Query(final String query) {
        this.tokens = TokenizerFactory.createQueryTokenizer().tokenize(query);
        this.iterator = tokens.listIterator();
    }

    @SuppressWarnings("unchecked")
    private Query(final List<Token> tokens) {
        this.tokens = (List<Token>) SerializationUtils.roundtrip((Serializable) tokens);
        this.iterator = tokens.listIterator();
    }

    public static Query from(final String query) {
        return new Query(query);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Token next() {
        return iterator.next();
    }

    public String nextPath() {
        final StringBuilder path = new StringBuilder();
        while (hasNext()) {
            final Token currentToken = next();
            if (Token.DOT_SYMBOL != currentToken.getIndex()) {
                path.append(currentToken.getToken());
            } else {
                break;
            }
        }
        return path.toString();
    }

    public int leftTokens() {
        return tokens.size() - iterator.nextIndex();
    }

    Query fork() {
        final int nextIndex = iterator.nextIndex();
        final int size = tokens.size();
        if (nextIndex < size) {
            return new Query(Lists.newLinkedList(tokens.subList(nextIndex, size)));
        }
        throw new EndOfQueryException();
    }
}
