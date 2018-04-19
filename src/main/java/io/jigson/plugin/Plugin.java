package io.jigson.plugin;

import io.jigson.pipe.Flow;

public interface Plugin<T, R> extends Flow<T, R> {

    String getKey();

}
