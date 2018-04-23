package io.jigson.pipe;

@FunctionalInterface
public interface ContextFlow<T, R> extends Flow<T, R> {

    static <A, B> ContextFlow<A, B> fromFlow(final Flow<A, B> flow) {
        return flow::flow;
    }

    default R flow(final T input, final JigsonContext context) {
        return flow(input);
    }
}
