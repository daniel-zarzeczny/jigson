/*
 *    Copyright 2018 Daniel Zarzeczny
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.jigson.pipe;


import io.jigson.expression.predicate.Predicate;

import java.util.Optional;
import java.util.function.Supplier;

import static io.jigson.utils.JsonUtils.getMapper;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class JoinPipe<T> {

    private final Source<T> source;

    private JoinPipe(final Source<T> source) {
        this.source = source;
    }

    public static <U> JoinPipe<U> from(final Source<U> source) {
        return new JoinPipe<>(source);
    }

    public <U> JoinPipe<U> map(final Flow<T, U> flow) {
        final U output = flow.flow(source.get());
        return JoinPipe.from(Source.of(output));
    }

    public <U> JoinPipe<U> map(Supplier<Flow<T, U>> supplier) {
        return map(supplier.get());
    }

    public <R> Sink<R> flush(final Flow<T, R> flow) {
        final R output = flow.flow(source.get());
        return Sink.of(output);
    }

    public <R> Sink<R> flush(Supplier<Flow<T, R>> supplier) {
        return flush(supplier.get());
    }

    public boolean match(final Predicate<T> predicate) {
        return predicate.accept(source.get());
    }

    public JoinPipe<Boolean> matchThen(final Predicate<T> predicate) {
        final boolean match = predicate.accept(source.get());
        return JoinPipe.from(Source.of(match));
    }

    public String json() {
        return get().map(getMapper()::toJson).orElse(EMPTY);
    }

    public Optional<T> get() {
        return Optional.of(source.get());
    }

}
