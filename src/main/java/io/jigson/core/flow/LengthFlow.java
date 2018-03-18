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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.jigson.json.pipe.ProcessingPipe;
import io.jigson.pipe.Flow;

import java.math.BigDecimal;
import java.util.Optional;

public class LengthFlow implements Flow<JsonElement, JsonPrimitive> {

    static final LengthFlow INSTANCE = new LengthFlow();
    private final static JsonPrimitive ZERO = asJsonPrimitive(BigDecimal.ZERO.intValue());

    private static JsonPrimitive asJsonPrimitive(final int value) {
        return new JsonPrimitive(value);
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
        return CountFlow.INSTANCE.flow(jsonElement);
    }
}
