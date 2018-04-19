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
import io.jigson.core.JigsonConfig;
import io.jigson.core.JigsonConfigHolder;
import io.jigson.plugin.JsonPlugin;

import java.math.BigDecimal;
import java.util.Objects;

public class AveragePlugin implements JsonPlugin {

    public static final AveragePlugin INSTANCE = new AveragePlugin();
    private static final String KEY = "avg";

    private final int precision;
    private final int roundingMode;

    private AveragePlugin() {
        final JigsonConfig config = JigsonConfigHolder.get();
        this.precision = config.numbers().precision();
        this.roundingMode = config.numbers().roundingMode();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public JsonPrimitive flow(final JsonElement jsonElement) {
        final BigDecimal sum = SumPlugin.INSTANCE.flow(jsonElement).getAsBigDecimal();
        if (Objects.nonNull(jsonElement) && jsonElement.isJsonArray()) {
            final BigDecimal divisor = new BigDecimal(jsonElement.getAsJsonArray().size());
            final BigDecimal average = sum.divide(divisor, precision, roundingMode);
            return asJsonPrimitive(average);
        }
        return asJsonPrimitive(sum);

    }

    private JsonPrimitive asJsonPrimitive(final BigDecimal value) {
        return new JsonPrimitive(value);
    }
}
