package io.jigson.plugin;

import com.google.gson.JsonElement;
import io.jigson.pipe.JigsonContext;

public interface JsonPlugin extends Plugin<JsonElement, JsonElement> {

    @Override
    default JsonElement flow(final JsonElement input) {
        return flow(input, JigsonContext.newContext());
    }
}
