package io.jigson.core;

import java.util.Objects;

public class JigsonConfigHolder {

    private static ThreadLocal<JigsonConfig> configHolder = new ThreadLocal<>();

    static {
        init();
    }

    private JigsonConfigHolder() {
    }

    public static void init() {
        configHolder.set(JigsonConfig.newInstance());
    }

    public static void set(final JigsonConfig config) {
        configHolder.set(config);
    }

    public static JigsonConfig get() {
        JigsonConfig config = configHolder.get();
        if (Objects.isNull(config)) {
            config = JigsonConfig.newInstance();
            configHolder.set(config);
        }
        return config;
    }

}
