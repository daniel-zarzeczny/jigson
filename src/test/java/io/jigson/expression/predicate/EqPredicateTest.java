package io.jigson.expression.predicate;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class EqPredicateTest {


    @Test
    public void shouldAccept_WhenTwoTextsAreEqual() {

        // given
        final String left = "abc";
        final String right = "abc";

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isTrue();
    }

    @Test
    public void shouldNotAccept_WhenTwoTextsAreNotEqual() {

        // given
        final String left = "abc";
        final String right = "cba";

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isFalse();
    }

    @Test
    public void shouldNotAccept_WhenLeftIsNull() {

        // given
        final String left = null;
        final String right = "abc";

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isFalse();

    }

    @Test
    public void shouldNotAccept_WhenRightIsNull() {

        // given
        final String left = "abc";
        final String right = null;

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isFalse();

    }

    @Test
    public void shouldAccept_WhenBothAreNull() {

        // given
        final String left = null;
        final String right = null;

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isTrue();

    }

    @Test
    public void shouldAccept_WhenTwoIntegersAreEqual() {

        // given
        final Integer left = 1;
        final Integer right = 1;

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isTrue();
    }

    @Test
    public void shouldNotAccept_WhenTwoIntegersAreNotEqual() {

        // given
        final Integer left = 1;
        final Integer right = 10;

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isFalse();
    }

    @Test
    public void shouldAccept_WhenNumericEqualStringsAreGiven() {

        // given
        final String left = "001";
        final String right = "1";

        // when
        final boolean isEqual = new EqualPredicate<>(left).accept(right);

        // then
        assertThat(isEqual).isTrue();
    }
}
