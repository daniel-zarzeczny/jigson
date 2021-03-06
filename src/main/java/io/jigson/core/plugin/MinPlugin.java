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

package io.jigson.core.plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.jigson.json.pipe.ProcessingPipe;
import io.jigson.pipe.JigsonContext;
import io.jigson.plugin.JsonPlugin;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static io.jigson.utils.NumberUtils.MAX_VALUE;
import static io.jigson.utils.NumberUtils.MIN_VALUE;
import static io.jigson.utils.StreamUtils.not;

public class MinPlugin implements JsonPlugin {

    public static final MinPlugin INSTANCE = new MinPlugin();
    private static final String KEY = "min";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public JsonElement flow(final JsonElement jsonElement) {
        return flow(jsonElement, JigsonContext.newContext());
    }

    @Override
    public JsonPrimitive flow(final JsonElement jsonElement, final JigsonContext context) {

        final Optional<JsonElement> min =
                ProcessingPipe
                        .from(jsonElement)
                        .whenNullOrJsonNull(this::handleNull)
                        .whenJsonPrimitive(this::handlePrimitive)
                        .whenJsonObject(this::handleObject)
                        .whenJsonArray(this::handleArray)
                        .process()
                        .get();
        return min.map(JsonElement::getAsJsonPrimitive).orElse(asJsonPrimitive(MIN_VALUE));
    }

    private JsonPrimitive handleNull(final JsonElement jsonElement) {
        throw new IllegalJsonElementException("Cannot execute min() on JsonNull!");
    }

    private JsonPrimitive handlePrimitive(final JsonElement jsonElement) {
        return asJsonPrimitive(jsonElement.getAsBigDecimal());
    }

    private JsonPrimitive handleObject(final JsonElement jsonElement) {
        throw new IllegalJsonElementException("Cannot execute min() on JsonObject!");
    }

    private JsonPrimitive handleArray(final JsonElement jsonElement) {
        final JsonArray jsonArray = jsonElement.getAsJsonArray();
        final BigDecimal min =
                IntStream.range(0, jsonArray.size())
                        .mapToObj(jsonArray::get)
                        .filter(Objects::nonNull)
                        .filter(not(JsonElement::isJsonNull))
                        .map(this::flow)
                        .filter(JsonElement::isJsonPrimitive)
                        .map(JsonElement::getAsBigDecimal)
                        .reduce(MAX_VALUE, BigDecimal::min);
        return asJsonPrimitive(min);
    }

    private JsonPrimitive asJsonPrimitive(final BigDecimal value) {
        return new JsonPrimitive(value);
    }
}
