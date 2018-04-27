package io.jigson.pipe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

public class JigsonContext extends HashMap<String, Object> {


    private JigsonContext() {
    }

    public static JigsonContext newContext() {
        return new JigsonContext();
    }

    public Optional<Integer> getInt(final String key) {
        return get(key, Integer.class);
    }

    public Optional<Long> getLong(final String key) {
        return get(key, Long.class);
    }

    public Optional<Double> getDouble(final String key) {
        return get(key, Double.class);
    }

    public Optional<BigDecimal> getBigDecimal(final String key) {
        return get(key, BigDecimal.class);
    }

    public Optional<String> getString(final String key) {
        return get(key, String.class);
    }

    public <T> Optional<T> get(final String key, final Class<T> clazz) {
        return getObject(key).map(clazz::cast);
    }

    private Optional<Object> getObject(final String key) {
        return Optional.ofNullable(get(key));
    }
}
