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

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import io.jigson.expression.predicate.Predicate;
import io.jigson.plugin.JsonPlugin;
import io.jigson.plugin.PluginRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class PluginDispatcher {


    private PluginDispatcher() {
    }

    public static JsonElement dispatch(final JsonElement jsonElement, final String pluginKey) {

        if (Objects.isNull(jsonElement)) {
            throw new IllegalArgumentException();
        }
        return new Router(PluginRegistry.INSTANCE).route(pluginKey, jsonElement);
    }


    private static class Router {

        private final Map<RoutingPredicate, JsonPlugin> routingRules = Maps.newHashMap();

        private Router(final PluginRegistry registry) {
            registry.getPlugins().forEach(this::withPlugin);
        }

        private Router withPlugin(final JsonPlugin plugin) {
            final RoutingPredicate predicate = new RoutingPredicate(plugin);
            routingRules.put(predicate, plugin);
            return this;
        }

        private JsonElement route(final String key, final JsonElement jsonElement) {

            return
                    routingRules
                            .keySet()
                            .stream()
                            .filter(predicate -> predicate.accept(key))
                            .findFirst()
                            .map(routingRules::get)
                            .map(plugin -> plugin.flow(jsonElement))
                            .orElseThrow(UnrecognizedPluginException::new);
        }
    }

    private static class RoutingPredicate implements Predicate<String> {

        private final String pluginKey;

        private RoutingPredicate(final JsonPlugin plugin) {
            this.pluginKey = plugin.getKey();
        }

        @Override
        public boolean accept(final String key) {
            return StringUtils.isNotBlank(key) && key.startsWith(pluginKey);
        }
    }

}
