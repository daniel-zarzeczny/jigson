package io.jigson.core;

import io.jigson.core.plugin.*;
import io.jigson.plugin.JsonPlugin;
import io.jigson.plugin.PluginRegistry;

public final class PluginsConfig {

    private final PluginRegistry pluginRegistry;

    private PluginsConfig() {
        this.pluginRegistry = PluginRegistry.INSTANCE;
    }

    static PluginsConfig newConfig() {
        return new PluginsConfig();
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
}
