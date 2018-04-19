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

import java.util.stream.IntStream;

import static io.jigson.utils.StreamUtils.not;

public class OnlyMatchingStrategy extends AbstractJsonArrayFilterStrategy {

    static final OnlyMatchingStrategy INSTANCE = new OnlyMatchingStrategy();

    private OnlyMatchingStrategy() {
    }

    @Override
    public JsonElement filter(final JsonArray jsonArray, final String criterion) {
        final JsonArray filteredArray = new JsonArray();
        IntStream
                .range(STARTING_INDEX, jsonArray.size())
                .mapToObj(jsonArray::get)
                .filter(not(JsonElement::isJsonPrimitive))
                .map(item -> filterWithRouting(item, criterion))
                .filter(not(JsonElement::isJsonNull))
                .forEach(filteredArray::add);
        return filteredArray.size() > 0 ? filteredArray : JsonNull.INSTANCE;
    }
}
