package io.jigson;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import io.jigson.core.Jigson;
import io.jigson.core.PluginsConfig;
import io.jigson.pipe.JigsonContext;
import io.jigson.plugin.JsonPlugin;
import io.jigson.plugin.PluginRegistry;
import org.junit.Test;

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
            public JsonElement flow(final JsonElement input, final JigsonContext context) {
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
        final JsonNull input = JsonNull.INSTANCE;

        final String counterKey = "counterValue";
        final int counterValue = 0;

        final JigsonContext context = JigsonContext.newContext();
        context.put(counterKey, counterValue);

        final JsonPlugin newPlugin = new JsonPlugin() {

            @Override
            public String getKey() {
                return pluginKey;
            }

            @Override
            public JsonElement flow(final JsonElement input, final JigsonContext context) {
                Integer counter = context.getInt(counterKey).orElseThrow(IllegalArgumentException::new);
                return new JsonPrimitive(++counter);
            }

        };

        // when
        final Integer actualCounter =
                Jigson
                        .from(input)
                        .pluginsConfig()
                        .registerPlugin(newPlugin)
                        .and()
                        .parseThen("@increment()", context)
                        .mapToObj(JsonElement::getAsInt)
                        .get()
                        .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actualCounter).isNotNull();
        assertThat(actualCounter).isEqualTo(counterValue + 1);
    }
}
