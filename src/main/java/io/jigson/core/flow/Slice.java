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

import io.jigson.core.Token;
import io.jigson.core.TokenizerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public class Slice {

    private static final String COLON = ":";

    private int startIndex = INDEX_NOT_FOUND;
    private int endIndex = INDEX_NOT_FOUND;
    private int step = INDEX_NOT_FOUND;

    private Slice(final List<String> items) {

        if (CollectionUtils.isEmpty(items)) {
            throw new IllegalArgumentException();
        }
        String firstColon = EMPTY;
        String lastColon = EMPTY;

        for (final String item : items) {
            if (COLON.equals(item)) {
                if (isBlank(firstColon)) {
                    firstColon = COLON;
                } else {
                    lastColon = COLON;
                }
            } else if (StringUtils.isNumeric(item)) {
                if (isBlank(firstColon) && startIndex == INDEX_NOT_FOUND) {
                    startIndex = Math.abs(Integer.parseInt(item));
                } else if (isBlank(lastColon) && endIndex == INDEX_NOT_FOUND) {
                    endIndex = Math.abs(Integer.parseInt(item));
                } else if (step == INDEX_NOT_FOUND) {
                    step = Math.abs(Integer.parseInt(item));
                }
            }
        }
    }

    public static Slice from(final String rawIndex) {
        final List<String> items =
                TokenizerFactory.createSliceTokenizer()
                        .tokenize(rawIndex)
                        .stream()
                        .map(Token::getToken)
                        .collect(Collectors.toList());
        return new Slice(items);
    }

    public int getStartIndex() {
        return startIndex == INDEX_NOT_FOUND ? 0 : startIndex;
    }

    public int getEndIndex(final int size) {
        if (endIndex < 0 || endIndex > size) {
            return size;
        }
        return endIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getStep() {
        return step == INDEX_NOT_FOUND ? 1 : step;
    }
}
