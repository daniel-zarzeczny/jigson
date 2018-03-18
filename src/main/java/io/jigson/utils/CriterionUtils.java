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

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CriterionUtils {

    static final String OPEN_PREDICATE = "(";
    private static final String CLOSE_PREDICATE = ")";
    private static final String CRITERION_PATTERN = "[a-zA-Z]+(>|=|<|>=|<=|!=)[a-zA-Z0-9]+((&&|\\|\\|)[a-zA-Z]+(>|=|<|>=|<=|!=)[a-zA-Z0-9]+)*";

    public static String findCriterion(final String query) {

        final int openCriterionPosition = query.indexOf(OPEN_PREDICATE);
        final int closeCriterionPosition = query.indexOf(CLOSE_PREDICATE);

        if (isCorrectCriterionNotation(openCriterionPosition, closeCriterionPosition)) {
            return query.substring(openCriterionPosition + 1, closeCriterionPosition).trim();
        }
        return EMPTY;
    }

    private static boolean isCorrectCriterionNotation(final int openCriterionPosition, final int closeCriterionPosition) {
        return openCriterionPosition > 0 && closeCriterionPosition > openCriterionPosition;
    }

    public static boolean isCriterion(final String candidate) {
        return Pattern.matches(CRITERION_PATTERN, candidate);
    }
}
