package io.jigson.plugin;

import io.jigson.pipe.ContextFlow;
import io.jigson.pipe.JigsonContext;

public interface Plugin<T, R> extends ContextFlow<T, R> {

    String getKey();

    R flow(final T input, final JigsonContext context);

    R flow(T input);
}
