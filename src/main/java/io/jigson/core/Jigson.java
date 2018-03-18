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

package io.jigson.core;

import com.google.gson.JsonElement;
import io.jigson.config.Context;
import io.jigson.core.flow.FetchFlow;
import io.jigson.core.flow.FilterFlow;
import io.jigson.core.flow.Query;
import io.jigson.json.pipe.JsonPipe;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class Jigson {

    private final JsonElement jsonElement;
    private Context context = Context.newContext();

    private Jigson(final JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    public static Jigson from(final JsonElement jsonElement) {
        return new Jigson(jsonElement);
    }

    public JsonElement parse(final String rawQuery) {

        final String trimmedQuery = Optional.ofNullable(rawQuery).orElse(EMPTY);

        if (isNotBlank(trimmedQuery)) {

            final Query query = Query.from(trimmedQuery);
            final Token initialToken = query.next();

            if (Token.DOLLAR_SYMBOL == initialToken.getIndex()) {
                return new FilterFlow(query, context).flow(jsonElement);
            } else if (Token.AT_SYMBOL == initialToken.getIndex()) {
                return new FetchFlow(query, context).flow(jsonElement);
            }
            throw new IllegalPrefixTokenException();
        }
        throw new UnexpectedSymbolException();
    }

    public JsonPipe parseThen(final String rawQuery) {
        final JsonElement result = parse(rawQuery);
        return JsonPipe.from(result).withContext(context);
    }

    public Jigson withContext(final Context context) {
        this.context = context;
        return this;
    }
}
