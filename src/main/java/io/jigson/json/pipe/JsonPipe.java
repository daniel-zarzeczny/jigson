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
import com.google.gson.JsonNull;
import io.jigson.config.Context;
import io.jigson.expression.predicate.Predicate;
import io.jigson.json.expression.JsonPredicate;
import io.jigson.pipe.*;

import java.util.Optional;
import java.util.function.Supplier;

import static io.jigson.utils.JsonUtils.getMapper;

public class JsonPipe {

    private final Source<JsonElement> source;
    private Context context = Context.newContext();

    private JsonPipe(final Source<JsonElement> source) {
        this.source = source;
    }

    public static JsonPipe from(final JsonElement jsonElement) {
        return new JsonPipe(Source.of(jsonElement));
    }

    public JsonPipe withContext(final Context context) {
        this.context = context;
        return this;
    }

    public <R> Sink<R> flush(final Flow<JsonElement, R> flow) {
        return JoinPipe.from(source).flush(flow);
    }

    public <R> Sink<R> flush(final Supplier<Flow<JsonElement, R>> supplier) {
        return flush(supplier.get());
    }

    public <R> JoinPipe<R> mapJoin(final Flow<JsonElement, R> flow) {
        return JoinPipe.from(source).map(flow);
    }

    public <R> JoinPipe<R> mapJoin(final Supplier<Flow<JsonElement, R>> supplier) {
        return mapJoin(supplier.get());
    }

    public JsonPipe map(final UnitaryFlow<JsonElement> flow) {
        final JsonElement output = flush(flow).get();
        return JsonPipe.from(output);
    }

    public JsonPipe map(final Supplier<UnitaryFlow<JsonElement>> supplier) {
        return map(supplier.get());
    }

    public JsonPipe filter(final String criterion) {
        final FilterFlow filterFlow = new FilterFlow(criterion).withContext(context);
        return map(filterFlow);
    }

    public boolean match(final Predicate<JsonElement> predicate) {
        return JoinPipe.from(source).match(predicate);
    }

    public boolean match(final String criterion) {
        final JsonPredicate predicate = JsonPredicate.from(criterion).withContext(context);
        return match(predicate);
    }

    public String json() {
        final JsonElement jsonElement = get().orElse(JsonNull.INSTANCE);
        return getMapper().toJson(jsonElement);
    }

    public Optional<JsonElement> get() {
        return Optional.ofNullable(source.get());
    }
}
