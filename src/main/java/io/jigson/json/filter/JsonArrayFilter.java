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

package io.jigson.json.filter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

@FunctionalInterface
public interface JsonArrayFilter extends JsonFilter<JsonArray, JsonElement> {

    enum Strategy {

        /**
         * When at least one of {@link JsonArray} elements matches criterion,
         * then the output is exactly the same {@link JsonArray} as one passed on the input.
         * Otherwise {@link JsonNull} is returned.
         */
        ALL_IF_ANY_MATCHING,

        /**
         * The result {@link JsonArray} contains only elements
         * meeting filter criterion and all primitives regardless their values.
         * If none {@link JsonObject} matches criterion and there is no other elements,
         * then {@link JsonNull} is returned.
         */
        KEEP_MATCHING_AND_PRIMITIVES,


        /**
         * The result contains only {@link JsonObject}s meeting filter criterion.
         * All primitives (if any) are excluded from the result set.
         * If none {@link JsonObject} matches criterion, then {@link JsonNull} is returned.
         */
        ONLY_MATCHING
    }
}
