package io.jigson.json.expression;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;

public class JsonPredicateTest {

    private static final String JSON = "{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}";
    private JsonObject jsonObject;

    @Before
    public void init() {
        this.jsonObject = getMapper().fromJson(JSON, JsonObject.class);
    }

    @Test
    public void shouldAccept_WhenFirstNameCriterionIsMet() {

        // given
        // when
        final boolean isAccepted = JsonPredicate.from("firstName=John").accept(jsonObject);

        // then
        assertThat(isAccepted).isTrue();
    }

    @Test
    public void shouldNotAccept_WhenFirstNameCriterionIsNotMet() {

        // given
        // when
        final boolean isAccepted = JsonPredicate.from("firstName=Jack").accept(jsonObject);

        // then
        assertThat(isAccepted).isFalse();
    }


    @Test
    public void shouldAccept_WhenIs25() {

        // given
        // when
        final boolean isAccepted = JsonPredicate.from("age=25").accept(jsonObject);

        // then
        assertThat(isAccepted).isTrue();
    }

    @Test
    public void shouldAccept_WhenIsNot25() {

        // given
        final JsonObject jsonObject = getMapper().fromJson(JSON, JsonObject.class);

        // when
        final boolean isAccepted = JsonPredicate.from("age!=25").accept(jsonObject);

        // then
        assertThat(isAccepted).isFalse();
    }
}
