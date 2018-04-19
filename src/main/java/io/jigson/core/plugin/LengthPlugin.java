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
import io.jigson.plugin.JsonPlugin;

import java.math.BigDecimal;
import java.util.Optional;

public class LengthPlugin implements JsonPlugin {

    public static final LengthPlugin INSTANCE = new LengthPlugin();
    private static final String KEY = "length";
    private static final JsonPrimitive ZERO = asJsonPrimitive(BigDecimal.ZERO.intValue());

    private static JsonPrimitive asJsonPrimitive(final int value) {
        return new JsonPrimitive(value);
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public JsonPrimitive flow(final JsonElement jsonElement) {

        final Optional<JsonElement> length =
                ProcessingPipe
                        .from(jsonElement)
                        .whenNullOrJsonNull(this::handleNullOrJsonNull)
                        .whenJsonPrimitive(this::handlePrimitive)
                        .whenJsonObject(this::handleObject)
                        .whenJsonArray(this::handleArray)
                        .process()
                        .get();
        return length
                .map(JsonElement::getAsJsonPrimitive)
                .orElse(ZERO);
    }

    private JsonPrimitive handleNullOrJsonNull(final JsonElement jsonElement) {
        return ZERO;
    }

    private JsonPrimitive handlePrimitive(final JsonElement jsonElement) {
        final int length = jsonElement.getAsString().length();
        return asJsonPrimitive(length);
    }

    private JsonPrimitive handleObject(final JsonElement jsonElement) {
        throw new IllegalJsonElementException("Cannot execute length() on JsonObject!");

    }

    private JsonPrimitive handleArray(final JsonElement jsonElement) {
        return CountPlugin.INSTANCE.flow(jsonElement);
    }
}
