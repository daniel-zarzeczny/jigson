package io.jigson.core;

import io.jigson.core.plugin.*;
import io.jigson.plugin.JsonPlugin;
import io.jigson.plugin.PluginRegistry;

public final class PluginsConfig {

    private final Jigson jigson;
    private final PluginRegistry pluginRegistry;

    PluginsConfig(final Jigson jigson) {
        this.jigson = jigson;
        this.pluginRegistry = PluginRegistry.INSTANCE;
    }

    void registerEmbeddedPlugins() {
        registerPlugin(LengthPlugin.INSTANCE);
        registerPlugin(CountPlugin.INSTANCE);
        registerPlugin(SizePlugin.INSTANCE);
        registerPlugin(SumPlugin.INSTANCE);
        registerPlugin(MinPlugin.INSTANCE);
        registerPlugin(MaxPlugin.INSTANCE);
        registerPlugin(AveragePlugin.INSTANCE);
    }

    public PluginsConfig registerPlugin(final JsonPlugin plugin) {
        pluginRegistry.register(plugin);
        return this;
    }

    public PluginsConfig deregisterPlugin(final String key) {
        pluginRegistry.deregister(key);
        return this;
    }

    public Jigson and() {
        return jigson;
    }
}
