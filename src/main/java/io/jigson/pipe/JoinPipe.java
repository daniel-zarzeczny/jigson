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

public class JoinPipe<T> implements Pipe<T> {

    protected final Source<T> source;

    protected JoinPipe(final Source<T> source) {
        this.source = source;
    }

    public static <U> JoinPipe<U> from(final Source<U> source) {
        return new JoinPipe<>(source);
    }

    @Override
    public <U, R> Pipe<R> join(final Flow<T, U> primary, final Flow<U, R> secondary) {
        final R output = secondary.flow(primary.flow(source.getOutput()));
        return new JoinPipe<>(Source.of(output));
    }

    @Override
    public <U> Pipe<U> concat(final Flow<T, U> flow) {
        final U output = flow.flow(source.getOutput());
        return new JoinPipe<>(Source.of(output));
    }

    @Override
    public <R> Sink<R> flush(final Flow<T, R> flow) {
        final R output = flow.flow(source.getOutput());
        return Sink.of(output);
    }

    @Override
    public boolean match(final Predicate<T> predicate) {
        return predicate.accept(source.getOutput());
    }

    @Override
    public Pipe<Boolean> matchThen(final Predicate<T> predicate) {
        final boolean match = predicate.accept(source.getOutput());
        return new JoinPipe<>(Source.of(match));
    }

    @Override
    public Optional<T> get() {
        return Optional.of(source.getOutput());
    }

    @Override
    public void println() {
        System.out.println(source.getOutput());
    }

}
