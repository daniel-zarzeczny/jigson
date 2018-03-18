package io.jigson.json.filter.strategy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;

public class KeepMatchingAndPrimitivesStrategyTest {

    private static final String JSON_ARRAY =
            "[{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}," +
                    "{\"firstName\":\"Sansa\",\"lastName\":\"Stark\", \"age\" : 20}]";
    private static final String JSON_ARRAY_WITH_PRIMITIVES =
            "[\"Winterfell\", \"Castle Black\",{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}," +
                    "{\"firstName\":\"Sansa\",\"lastName\":\"Stark\", \"age\" : 20}]";

    private final KeepMatchingAndPrimitivesStrategy filter = KeepMatchingAndPrimitivesStrategy.INSTANCE;

    private JsonArray jsonArray;
    private JsonArray jsonArrayWithPrimitives;

    @Before
    public void init() {
        this.jsonArray = getMapper().fromJson(JSON_ARRAY, JsonArray.class);
        this.jsonArrayWithPrimitives = getMapper().fromJson(JSON_ARRAY_WITH_PRIMITIVES, JsonArray.class);
    }

    @Test
    public void shouldFilterOutElement_WhenOneDoesNotMatchCriterion() {

        // given
        final String criterion = "firstName=John";

        // when
        final JsonElement actualElement = filter.filter(jsonArray, criterion);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isFalse();
        assertThat(actualElement).isNotSameAs(jsonArray);
        assertThat(actualElement.getAsJsonArray().size()).isEqualTo(1);
    }

    @Test
    public void shouldProduceJsonNull_WhenNoneObjectMatchesCriterion() {

        // given
        final String criterion = "age<10";

        // when
        final JsonElement actualElement = filter.filter(jsonArray, criterion);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isTrue();
        assertThat(actualElement).isNotSameAs(jsonArray);
    }

    @Test
    public void shouldFilterOutElementAndKeepPrimitives_WhenOneDoesNotMatchCriterion() {

        // given
        final String criterion = "firstName=John";

        // when
        final JsonElement actualElement = filter.filter(jsonArrayWithPrimitives, criterion);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isFalse();
        assertThat(actualElement).isNotSameAs(jsonArray);
        assertThat(actualElement.getAsJsonArray().size()).isEqualTo(3);
    }

    @Test
    public void shouldKeepOnlyPrimitives_WhenNoneObjectMeetsCriterion() {

        // given
        final String criterion = "age>50";

        // when
        final JsonElement actualElement = filter.filter(jsonArrayWithPrimitives, criterion);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isFalse();
        assertThat(actualElement).isNotSameAs(jsonArray);
        assertThat(actualElement.getAsJsonArray().size()).isEqualTo(2);
    }
}
