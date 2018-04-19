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

import com.google.gson.*;
import io.jigson.core.flow.Slice;
import io.jigson.core.plugin.IllegalJsonElementException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND;

public class JsonUtils {

    private static final Gson gson = new Gson();
    private static final int INDEX_ZERO = 0;

    public static Gson getMapper() {
        return gson;
    }

    public static JsonElement getPropertyByPath(final JsonObject jsonObject, final String path) {

        final String propertyName = PathUtils.findPropertyName(path);
        final JsonElement jsonElement =
                Optional.ofNullable(jsonObject.get(propertyName))
                        .orElse(JsonNull.INSTANCE);
        final String rawIndex = PathUtils.findRawIndex(path);
        final int index = PathUtils.findIndex(rawIndex);

        if (index != INDEX_NOT_FOUND) {
            if (!jsonElement.isJsonArray()) {
                throw new IllegalJsonElementException("Not an instance of JsonArray!");
            } else {
                return jsonElement.getAsJsonArray().get(index);
            }
        } else if (PathUtils.isSlice(rawIndex)) {

            final JsonArray jsonArray = jsonElement.getAsJsonArray();

            final Slice slice = Slice.from(rawIndex);
            final int startIndex = slice.getStartIndex();
            final int endIndex = slice.getEndIndex(jsonArray.size());
            final int step = slice.getStep();

            final JsonArray accumulator = new JsonArray();
            IntStream
                    .iterate(startIndex, i -> i + step)
                    .limit(endIndex)
                    .filter(i -> i < endIndex)
                    .mapToObj(jsonArray::get)
                    .forEach(accumulator::add);
            return accumulator;

        }
        return jsonElement;
    }


    public static void addOrRemoveObjectAttribute(final JsonObject jsonObject, final JsonElement attribute, final String path) {
        final String propertyName = PathUtils.findPropertyName(path);
        if (Objects.nonNull(attribute) && !attribute.isJsonNull()) {
            jsonObject.add(propertyName, attribute);
        } else {
            jsonObject.remove(propertyName);
        }
    }

    public static JsonArray clearJsonArray(final JsonArray jsonArray) {
        while (jsonArray.size() > 0) {
            jsonArray.remove(INDEX_ZERO);
        }
        return jsonArray;
    }
}
