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
import com.google.gson.JsonNull;
import io.jigson.config.Context;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FunctionFlowDispatcher {

    private static final String COUNT_PREFIX = "count";
    private static final String SIZE_PREFIX = "size";
    private static final String SUM_PREFIX = "sum";
    private static final String AVG_PREFIX = "avg";
    private static final String MIN_PREFIX = "min";
    private static final String MAX_PREFIX = "max";
    private static final String LENGTH_PREFIX = "length";

    private FunctionFlowDispatcher() {
    }

    static JsonElement dispatch(final JsonElement jsonElement, final String functionName, final Context context) {
        if (isBlank(functionName) || Objects.isNull(jsonElement)) {
            throw new IllegalArgumentException();
        } else if (functionName.startsWith(COUNT_PREFIX)) {
            return CountFlow.INSTANCE.flow(jsonElement);
        } else if (functionName.startsWith(SIZE_PREFIX)) {
            return SizeFlow.INSTANCE.flow(jsonElement);
        } else if (functionName.startsWith(SUM_PREFIX)) {
            return SumFlow.INSTANCE.flow(jsonElement);
        } else if (functionName.startsWith(AVG_PREFIX)) {
            return AverageFlow.withContext(context).flow(jsonElement);
        } else if (functionName.startsWith(MIN_PREFIX)) {
            return MinFlow.INSTANCE.flow(jsonElement);
        } else if (functionName.startsWith(MAX_PREFIX)) {
            return MaxFlow.INSTANCE.flow(jsonElement);
        } else if (functionName.startsWith(LENGTH_PREFIX)) {
            return LengthFlow.INSTANCE.flow(jsonElement);
        }
        return JsonNull.INSTANCE;
    }
}
