package io.jigson.pipe;

import io.jigson.expression.predicate.Predicate;

import java.util.Optional;
import java.util.function.Supplier;

public class ContextJoinPipe<T> {

    private final Source<T> source;
    private JigsonContext context = JigsonContext.newContext();

    private ContextJoinPipe(final Source<T> source) {
        this.source = source;
    }

    public static <U> ContextJoinPipe<U> from(final U source) {
        return new ContextJoinPipe<>(Source.of(source));
    }

    public static <U> ContextJoinPipe<U> from(final Source<U> source) {
        return new ContextJoinPipe<>(source);
    }

    public ContextJoinPipe<T> withContext(final JigsonContext context) {
        this.context = context;
        return this;
    }

    public <U> ContextJoinPipe<U> map(final ContextFlow<T, U> flow) {
        final U output = flow.flow(source.get(), context);
        return from(Source.of(output)).withContext(context);
    }

    public <U> ContextJoinPipe<U> map(final Supplier<ContextFlow<T, U>> supplier) {
        return map(supplier.get());
    }

    public <R> Sink<R> flush(final ContextFlow<T, R> flow) {
        final R output = flow.flow(source.get(), context);
        return Sink.of(output);
    }

    public <R> Sink<R> flush(final Supplier<ContextFlow<T, R>> supplier) {
        return flush(supplier.get());
    }

    public boolean match(final Predicate<T> predicate) {
        return JoinPipe.from(source).match(predicate);
    }

    public ContextJoinPipe<Boolean> matchThen(final Predicate<T> predicate) {
        final boolean match = match(predicate);
        return from(Source.of(match)).withContext(context);
    }

    public String json() {
        return JoinPipe.from(source).json();
    }

    public Optional<T> get() {
        return Optional.of(source.get());
    }
}
