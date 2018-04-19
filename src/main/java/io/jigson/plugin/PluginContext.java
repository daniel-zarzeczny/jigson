package io.jigson.plugin;

import java.util.HashMap;
import java.util.Optional;

public class PluginContext extends HashMap<String, Object> {

    private static final String INPUT = "input";

    private PluginContext() {
    }

    public static PluginContext newInstance() {
        return new PluginContext();
    }

    public void setInput(final Object input) {
        put(INPUT, input);
    }

    public <T> Optional<T> getInput(final Class<T> clazz) {
        return getObject(INPUT).map(clazz::cast);
    }

    public Optional<Integer> getInt(final String key) {
        return get(key, Integer.class);
    }

    public Optional<String> getString(final String key) {
        return get(key, String.class);
    }

    private <T> Optional<T> get(final String key, final Class<T> clazz) {
        return getObject(key).map(clazz::cast);
    }

    private Optional<Object> getObject(final String key) {
        return Optional.ofNullable(get(key));
    }
}
