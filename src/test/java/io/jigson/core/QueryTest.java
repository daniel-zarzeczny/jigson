package io.jigson.core;

import com.google.common.collect.Lists;
import io.jigson.core.flow.Query;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class QueryTest {

    @Test
    public void shouldGiveAllTokens_WhenItsNotEmpty() {

        // given
        final String rawQuery = "$people.address.city";
        final Query query = Query.from(rawQuery);
        final int expectedTokensAmount = 6;

        // when
        final List<Token> tokens = Lists.newLinkedList();
        query.forEachRemaining(tokens::add);

        // then
        assertThat(query.hasNext()).isFalse();
        assertThat(tokens).isNotEmpty();
        assertThat(tokens.size()).isEqualTo(expectedTokensAmount);
    }

    @Test
    public void shouldGiveAllPaths_WhenItsNotEmpty() {

        // given
        final String rawQuery = "$people.address.city";
        final Query query = Query.from(rawQuery);
        final int expectedPathsAmount = 3;

        // when
        final List<String> paths = Lists.newLinkedList();
        while (query.hasNext()) {
            paths.add(query.nextPath());
        }

        // then
        assertThat(query.hasNext()).isFalse();
        assertThat(paths).isNotEmpty();
        assertThat(paths.size()).isEqualTo(expectedPathsAmount);
    }

    @Test(expected = UnexpectedSymbolException.class)
    public void shouldNotGiveAnyPath_WhenRawQueryIsEmpty() {

        // given
        final String rawQuery = StringUtils.EMPTY;
        final Query query = Query.from(rawQuery);

        // when
        query.nextPath();
    }

    @Test(expected = UnexpectedSymbolException.class)
    public void shouldNotGiveAnyPath_WhenRawQueryIsNull() {

        // given
        final String rawQuery = null;
        final Query query = Query.from(rawQuery);

        // when
        query.nextPath();
    }
}
