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

package io.jigson.json.pipe;

import com.google.gson.JsonElement;
import io.jigson.config.Context;
import io.jigson.json.expression.JsonPredicate;
import io.jigson.pipe.JoinPipe;
import io.jigson.pipe.Source;

public class JsonPipe extends JoinPipe<JsonElement> {

    private Context context = Context.newContext();

    JsonPipe(final Source<JsonElement> source) {
        super(source);
    }

    public static JsonPipe from(final JsonElement jsonElement) {
        return new JsonPipe(Source.of(jsonElement));
    }

    public JsonPipe withContext(final Context context) {
        this.context = context;
        return this;
    }

    public JsonPipe filter(final String criterion) {
        final JsonElement sourceOutput = source.getOutput();
        final JsonElement flowOutput = new FilterFlow(criterion).withContext(context).flow(sourceOutput);
        return JsonPipe.from(flowOutput).withContext(context);
    }

    public boolean match(final String criterion) {
        final JsonPredicate predicate = JsonPredicate.from(criterion).withContext(context);
        return match(predicate);
    }
}
