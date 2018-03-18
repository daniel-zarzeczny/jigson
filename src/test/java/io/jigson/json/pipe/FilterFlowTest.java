package io.jigson.json.pipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class FilterFlowTest {

    private static final String JSON_OBJECT = "{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}";
    private static final String JSON_ARRAY =
            "[{\"firstName\":\"John\",\"lastName\":\"Snow\", \"age\" : 25}," +
                    "{\"firstName\":\"Sansa\",\"lastName\":\"Stark\", \"age\" : 20}]";

    private JsonObject jsonObject;
    private JsonArray jsonArray;

    @Before
    public void init() {
        this.jsonObject = getMapper().fromJson(JSON_OBJECT, JsonObject.class);
        this.jsonArray = getMapper().fromJson(JSON_ARRAY, JsonArray.class);
    }

    @Test
    public void shouldPassThrough_WhenMeetsCriteria() {

        // given
        final String criterion = "firstName=John";

        // when
        final JsonElement actualElement = new FilterFlow(criterion).flow(jsonObject);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonObject()).isTrue();
        assertThat(actualElement).isSameAs(jsonObject);
    }

    @Test
    public void shouldNotPassThrough_WhenDoesNotMeetCriteria() {

        // given
        final String criterion = "firstName=Jack";

        // when
        final JsonElement actualElement = new FilterFlow(criterion).flow(jsonObject);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isTrue();
        assertThat(actualElement).isNotSameAs(jsonObject);
    }

    @Test
    public void shouldPassThrough_WhenGivenArrayAndAtLeastOneItemMeetsCriteria() {

        // given
        final String criterion = "firstName=John";

        // when
        final JsonElement actualElement = new FilterFlow(criterion).flow(jsonArray);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonArray()).isTrue();
        assertThat(actualElement).isSameAs(jsonArray);
    }

    @Test
    public void shouldNotPassThrough_WhenGivenArrayAndNoneItemMeetsCriteria() {

        // given
        final String criterion = "firstName=Johnny";

        // when
        final JsonElement actualElement = new FilterFlow(criterion).flow(jsonArray);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isTrue();
        assertThat(actualElement).isNotSameAs(jsonArray);
    }

    @Test
    public void shouldGetJsonNull_WhenJsonElementIsNull() {

        // given
        final String criterion = "firstName=John";

        // when
        final JsonElement actualElement = new FilterFlow(criterion).flow(null);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonNull()).isTrue();
        assertThat(actualElement).isNotSameAs(jsonArray);
    }

    @Test
    public void shouldGetSameElement_WhenCriteriaIsBlank() {

        // given
        // when
        final JsonElement actualElement = new FilterFlow(EMPTY).flow(jsonArray);

        // then
        assertThat(actualElement).isNotNull();
        assertThat(actualElement.isJsonArray()).isTrue();
        assertThat(actualElement).isSameAs(jsonArray);
    }

    @Test(expected = IllegalCriteriaException.class)
    public void shouldThrowException_WhenCriteriaHasNotPropertyValue() {

        // given
        final String criterion = "firstName=";

        // when
        new FilterFlow(criterion).flow(jsonArray);
    }

    @Test(expected = IllegalCriteriaException.class)
    public void shouldThrowException_WhenCriteriaHasNotPropertyName() {

        // given
        final String criterion = "=John";

        // when
        new FilterFlow(criterion).flow(jsonArray);
    }

    @Test(expected = IllegalCriteriaException.class)
    public void shouldThrowException_WhenCriteriaHasNotOperator() {

        // given
        final String criterion = "firstNameJohn";

        // when
        new FilterFlow(criterion).flow(jsonArray);
    }
}
