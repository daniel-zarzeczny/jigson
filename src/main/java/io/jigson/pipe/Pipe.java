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

public interface Pipe<T> {

    <U, R> Pipe<R> join(Flow<T, U> primary, Flow<U, R> secondary);

    <U> Pipe<U> concat(Flow<T, U> flow);

    default <U> Pipe<U> concat(Supplier<Flow<T, U>> supplier) {
        return concat(supplier.get());
    }

    <R> Sink<R> flush(Flow<T, R> flow);

    default <R> Sink<R> flush(Supplier<Flow<T, R>> supplier) {
        return flush(supplier.get());
    }

    boolean match(Predicate<T> predicate);

    Pipe<Boolean> matchThen(Predicate<T> predicate);

    Optional<T> get();

    void println();
}
