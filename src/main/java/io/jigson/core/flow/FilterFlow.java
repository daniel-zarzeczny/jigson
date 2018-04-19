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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import io.jigson.core.JigsonConfigHolder;
import io.jigson.json.filter.JsonArrayFilter;
import io.jigson.json.filter.JsonObjectFilter;
import io.jigson.json.filter.strategy.JsonFilterStrategyFactory;
import io.jigson.pipe.UnitaryFlow;

import java.util.Objects;

public class FilterFlow implements UnitaryFlow<JsonElement> {

    private final String criterion;
    private final JsonObjectFilter jsonObjectFilter;

    public FilterFlow(final String criterion) {
        this.criterion = criterion;
        this.jsonObjectFilter = JsonObjectFilter.INSTANCE;
    }

    @Override
    public JsonElement flow(final JsonElement jsonElement) {
        return filter(jsonElement);
    }

    private JsonElement filter(final JsonElement jsonElement) {
        if (Objects.isNull(jsonElement) || jsonElement.isJsonNull()) {
            return JsonNull.INSTANCE;
        } else if (jsonElement.isJsonObject()) {
            return jsonObjectFilter.filter((JsonObject) jsonElement, criterion);
        } else if (jsonElement.isJsonArray()) {
            final JsonArrayFilter.Strategy filterStrategy = JigsonConfigHolder.get().filters().arrays().strategy();
            final JsonArrayFilter jsonArrayFilter = JsonFilterStrategyFactory.createJsonArrayFilter(filterStrategy);
            return jsonArrayFilter.filter((JsonArray) jsonElement, criterion);
        } else {
            return jsonElement;
        }
    }

}
