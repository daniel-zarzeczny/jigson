/*
 *    Copyright 2018 the original author or authors
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
import io.jigson.pipe.JigsonContext;
import io.jigson.plugin.JsonPlugin;

/**
 * Represents a flow responsible for resolving size of {@link JsonElement}
 *
 * @author Daniel Zarzeczny
 */
public class SizePlugin implements JsonPlugin {

    public static final SizePlugin INSTANCE = new SizePlugin();
    private static final String KEY = "size";

    private SizePlugin() {
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public JsonElement flow(final JsonElement jsonElement) {
        return flow(jsonElement, JigsonContext.newContext());
    }

    @Override
    public JsonElement flow(final JsonElement jsonElement, final JigsonContext context) {
        return CountPlugin.INSTANCE.flow(jsonElement, context);
    }
}
