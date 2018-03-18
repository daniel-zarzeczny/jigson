package io.jigson.pipe;

import io.jigson.expression.predicate.EqualPredicate;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class JoinPipeTest {

    @Test
    public void shouldSuccessfullyMatch_WhenEqualPredicateGiven() {

        // given
        final Integer left = 1;
        final Integer right = 1;

        // when
        final boolean isEqual = JoinPipe.from(Source.of(right)).match(new EqualPredicate<>(left));

        // then
        assertThat(isEqual).isTrue();
    }
}
