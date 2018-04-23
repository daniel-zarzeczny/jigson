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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.jigson.json.pipe.ProcessingPipe;
import io.jigson.pipe.JigsonContext;
import io.jigson.plugin.JsonPlugin;

import java.math.BigDecimal;
import java.util.Optional;

public class CountPlugin implements JsonPlugin {

    public static final CountPlugin INSTANCE = new CountPlugin();
    private static final String KEY = "count";

    private CountPlugin() {
    }

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

        final Optional<JsonElement> count =
                ProcessingPipe
                        .from(jsonElement)
                        .whenNullOrJsonNull(this::handleNull)
                        .whenJsonPrimitive(this::handlePrimitive)
                        .whenJsonObject(this::handleObject)
                        .whenJsonArray(this::handleArray)
                        .process()
                        .get();

        return count
                .map(JsonElement::getAsJsonPrimitive)
                .orElse(asPrimitive(BigDecimal.ZERO.intValue()));
    }

    private JsonPrimitive handleNull(final JsonElement jsonElement) {
        return asPrimitive(BigDecimal.ZERO.intValue());
    }

    private JsonPrimitive handlePrimitive(final JsonElement jsonElement) {
        return asPrimitive(BigDecimal.ONE.intValue());
    }

    private JsonPrimitive handleObject(final JsonElement jsonElement) {
        return asPrimitive(BigDecimal.ONE.intValue());
    }

    private JsonPrimitive handleArray(final JsonElement jsonElement) {
        final int arraySize = jsonElement.getAsJsonArray().size();
        return asPrimitive(arraySize);
    }

    private JsonPrimitive asPrimitive(final int value) {
        return new JsonPrimitive(value);
    }
}
