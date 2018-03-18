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

package io.jigson.json.filter.strategy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import io.jigson.config.Context;
import io.jigson.json.filter.JsonArrayFilter;
import io.jigson.json.filter.JsonObjectFilter;

abstract class AbstractJsonArrayFilterStrategy implements JsonArrayFilter {

    static final int STARTING_INDEX = 0;

    private final JsonObjectFilter jsonObjectFilter;

    AbstractJsonArrayFilterStrategy() {
        this.jsonObjectFilter = JsonObjectFilter.INSTANCE;
    }

    JsonElement filterWithRouting(final JsonElement jsonElement, final String criterion, final Context context) {
        if (jsonElement.isJsonPrimitive()) {
            // primitive is just passed through by default (no filtering)
            return jsonElement;
        } else if (jsonElement.isJsonObject()) {
            return jsonObjectFilter.filter((JsonObject) jsonElement, criterion, context);
        } else if (jsonElement.isJsonArray()) {
            return filter((JsonArray) jsonElement, criterion, context);
        } else {
            return JsonNull.INSTANCE;
        }
    }
}
