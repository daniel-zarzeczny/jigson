package io.jigson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.jigson.core.Jigson;
import io.jigson.core.PluginsConfig;
import io.jigson.plugin.JsonPlugin;
import io.jigson.plugin.PluginRegistry;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.truth.Truth.assertThat;

public class PluginTest {

    @Test
    public void shouldDeregisterExistingPlugin() {

        // given
        final String pluginKey = "count";

        // when

        final Jigson jigson = Jigson.from(JsonNull.INSTANCE);
        jigson.pluginsConfig().deregisterPlugin(pluginKey);

        // then
        final JsonPlugin plugin = PluginRegistry.INSTANCE.get(pluginKey);
        assertThat(plugin).isNull();
    }

    @Test
    public void shouldRegisterNewPlugin() {

        // given
        final String pluginKey = "brandNewPlugin";
        final JsonPlugin newPlugin = new JsonPlugin() {
            @Override
            public String getKey() {
                return pluginKey;
            }

            @Override
            public JsonElement flow(JsonElement input) {
                throw new UnsupportedOperationException();
            }
        };

        // when
        final Jigson jigson = Jigson.from(JsonNull.INSTANCE);
        final PluginsConfig config = jigson.pluginsConfig();
        config.registerPlugin(newPlugin);


        // then
        final JsonPlugin actualPlugin = PluginRegistry.INSTANCE.get(pluginKey);
        assertThat(actualPlugin).isNotNull();
        assertThat(pluginKey).isEqualTo(actualPlugin.getKey());
        assertThat(actualPlugin).isSameAs(newPlugin);
    }

    @Test
    public void shouldExecuteNewPlugin() {

        // given
        final String pluginKey = "increment";
        final AtomicInteger counter = new AtomicInteger(0);
        final JsonNull input = JsonNull.INSTANCE;

        final JsonPlugin newPlugin = new JsonPlugin() {
            @Override
            public String getKey() {
                return pluginKey;
            }

            @Override
            public JsonElement flow(final JsonElement input) {
                counter.incrementAndGet();
                return input;
            }
        };

        // when
        final Jigson jigson = Jigson.from(input);
        final PluginsConfig config = jigson.pluginsConfig();
        config.registerPlugin(newPlugin);

        final JsonElement output = jigson.parse("@increment()");

        // then
        assertThat(output).isNotNull();
        assertThat(output).isSameAs(input);
        assertThat(counter.get()).isEqualTo(1);
    }
}
