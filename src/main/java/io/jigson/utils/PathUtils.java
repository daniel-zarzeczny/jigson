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

package io.jigson.utils;

import static io.jigson.utils.CriterionUtils.OPEN_PREDICATE;
import static org.apache.commons.lang3.StringUtils.*;

public class PathUtils {

    private static final String EMPTY_BRACES = "()";
    private static final String OPEN_SQUARED_BRACKET = "[";
    private static final String CLOSE_SQUARED_BRACKET = "]";
    private static final String INDEX_SEPARATOR = ":";

    public static boolean isFunction(final String path) {
        return isNotBlank(path) && path.endsWith(EMPTY_BRACES);
    }

    public static int findIndex(final String rawIndex) {
        if (isNotBlank(rawIndex)) {
            if (!isSlice(rawIndex)) {
                return Integer.parseInt(rawIndex);
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static String findRawIndex(final String path) {
        if (isNotBlank(path)) {
            final int openBracketPosition = path.indexOf(OPEN_SQUARED_BRACKET);
            final int closeBracketPosition = path.indexOf(CLOSE_SQUARED_BRACKET);
            if (openBracketPosition > 0 && (closeBracketPosition > openBracketPosition + 1)) {
                return path.substring(openBracketPosition + 1, closeBracketPosition);
            }
        }
        return EMPTY;
    }

    public static boolean isSlice(final String rawIndex) {
        return isNotBlank(rawIndex) && rawIndex.contains(INDEX_SEPARATOR);
    }

    public static String findPropertyName(final String path) {
        final int openBracketPosition = path.indexOf(OPEN_SQUARED_BRACKET);
        if (openBracketPosition > 0) {
            return path.substring(0, openBracketPosition);
        }
        final int openCriterionPosition = path.indexOf(OPEN_PREDICATE);
        if (openCriterionPosition > 0) {
            return path.substring(0, openCriterionPosition);
        }
        return path.trim();
    }
}
