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
import io.jigson.pipe.Source;
import io.jigson.pipe.UnitaryFlow;

import java.util.Objects;
import java.util.function.Supplier;

public class ProcessingPipe extends JsonPipe {

    private Supplier<? extends RuntimeException> throwableSupplier;
    private UnitaryFlow<JsonElement> whenNullFlow;
    private UnitaryFlow<JsonElement> whenNullOrJsonNullFlow;
    private UnitaryFlow<JsonElement> whenJsonPrimitiveFlow;
    private UnitaryFlow<JsonElement> whenJsonObjectFlow;
    private UnitaryFlow<JsonElement> whenJsonArrayFlow;

    private ProcessingPipe(final Source<JsonElement> source) {
        super(source);
    }

    public static ProcessingPipe from(final JsonElement jsonElement) {
        return new ProcessingPipe(Source.of(jsonElement));
    }

    public <X extends RuntimeException> ProcessingPipe whenNullThrow(final Supplier<X> throwableSupplier) {
        this.throwableSupplier = throwableSupplier;
        return this;
    }

    public ProcessingPipe whenNull(final UnitaryFlow<JsonElement> flow) {
        this.whenNullFlow = flow;
        return this;
    }

    public ProcessingPipe whenNullOrJsonNull(final UnitaryFlow<JsonElement> flow) {
        this.whenNullOrJsonNullFlow = flow;
        return this;
    }

    public <X extends RuntimeException> ProcessingPipe whenNullOrJsonNullThrow(final Supplier<X> throwableSupplier) {
        this.throwableSupplier = throwableSupplier;
        return this;
    }

    public ProcessingPipe whenJsonPrimitive(final UnitaryFlow<JsonElement> flow) {
        this.whenJsonPrimitiveFlow = flow;
        return this;
    }

    public ProcessingPipe whenJsonObject(final UnitaryFlow<JsonElement> flow) {
        this.whenJsonObjectFlow = flow;
        return this;
    }

    public ProcessingPipe whenJsonArray(final UnitaryFlow<JsonElement> flow) {
        this.whenJsonArrayFlow = flow;
        return this;
    }

    public ProcessingPipe process() {

        final JsonElement jsonElement = source.getOutput();

        if (Objects.isNull(jsonElement)) {
            return processNull();
        } else if (jsonElement.isJsonNull()) {
            return processJsonNull(jsonElement);
        } else if (jsonElement.isJsonPrimitive()) {
            return executeFlow(whenJsonPrimitiveFlow, jsonElement);
        } else if (jsonElement.isJsonObject()) {
            return executeFlow(whenJsonObjectFlow, jsonElement);
        } else {
            return executeFlow(whenJsonArrayFlow, jsonElement);
        }
    }

    private ProcessingPipe processNull() {
        if (Objects.nonNull(throwableSupplier)) {
            throw throwableSupplier.get();
        } else if (Objects.nonNull(whenNullFlow)) {
            return executeFlow(whenNullFlow, null);
        } else {
            return executeFlow(whenNullOrJsonNullFlow, null);
        }
    }

    private ProcessingPipe processJsonNull(final JsonElement jsonElement) {
        if (Objects.nonNull(throwableSupplier)) {
            throw throwableSupplier.get();
        } else {
            return executeFlow(whenNullOrJsonNullFlow, jsonElement);
        }
    }

    private ProcessingPipe executeFlow(final UnitaryFlow<JsonElement> flow, final JsonElement jsonElement) {
        final JsonElement result = flow.flow(jsonElement);
        return ProcessingPipe.from(result);
    }
}
