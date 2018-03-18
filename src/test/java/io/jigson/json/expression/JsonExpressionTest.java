package io.jigson.json.expression;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;

public class JsonExpressionTest {

    private static final String JSON = "{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}";
    private JsonObject jsonObject;

    @Before
    public void init() {
        this.jsonObject = getMapper().fromJson(JSON, JsonObject.class);
    }

    @Test
    public void shouldAccept_WhenFirstAndLastNameCriteriaIsMet() {

        // given
        // when
        final boolean isAccepted = JsonExpression.from(jsonObject, "firstName=John&&lastName=Snow").interpret();

        // then
        assertThat(isAccepted).isTrue();
    }

    @Test
    public void shouldAccept_WhenFirstOrLastNameCriteriaIsMet() {

        // given
        // when
        final boolean isAccepted = JsonExpression.from(jsonObject, "firstName=John||lastName=Winter").interpret();

        // then
        assertThat(isAccepted).isTrue();
    }

    @Test
    public void shouldNotAccept_WhenFirstAndLastNameDoNotMeetCriteria() {

        // given
        // when
        final boolean isAccepted = JsonExpression.from(jsonObject, "firstName=Jack||lastName=Winter").interpret();

        // then
        assertThat(isAccepted).isFalse();
    }

    @Test
    public void shouldAccept_WhenIsBetween25And50() {

        // given
        final String JSON = "{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}";
        final JsonObject jsonObject = getMapper().fromJson(JSON, JsonObject.class);

        // when
        final boolean isAccepted = JsonExpression.from(jsonObject, "age>=25&&age<=50").interpret();

        // then
        assertThat(isAccepted).isTrue();
    }
}
