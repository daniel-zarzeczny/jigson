package io.jigson.plugin;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Set;

public class PluginRegistry extends HashMap<String, JsonPlugin> {

    public static final PluginRegistry INSTANCE = new PluginRegistry();

    private PluginRegistry() {
    }

    public void register(final JsonPlugin plugin) {
        putIfAbsent(plugin.getKey(), plugin);
    }

    public void deregister(final String key) {
        remove(key);
    }

    public Set<JsonPlugin> getPlugins() {
        return Sets.newHashSet(values());
    }

}
