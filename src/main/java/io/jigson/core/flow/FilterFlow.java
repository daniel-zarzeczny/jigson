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
import io.jigson.config.Context;
import io.jigson.pipe.UnitaryFlow;
import org.apache.commons.lang3.NotImplementedException;

public class FilterFlow implements UnitaryFlow<JsonElement> {

    private final Query query;
    private final Context context;

    public FilterFlow(final Query query, final Context context) {
        this.query = query.fork();
        this.context = context;
    }

    @Override
    public JsonElement flow(JsonElement object) {
        throw new NotImplementedException("Not ready yer!");
    }
}
