package io.jigson.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.jigson.config.Context;
import io.jigson.core.flow.IllegalJsonElementException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;

public class JigsonFetchTest {

    private static final String PEOPLE_JSON =
            "{\"people\":[{\"firstName\":\"John\",\"lastName\":\"Snow\",\"age\":\"25\",\"address\":{\"city\":\"Castle Black\"}}," +
                    "{\"firstName\":\"Sansa\",\"lastName\":\"Stark\",\"age\":\"20\",\"address\":{\"city\":\"Winterfell\"}}]}";
    private static final String PERSON_JSON =
            "{\"firstName\":\"Sansa\",\"lastName\":\"Stark\",\"age\":\"20\",\"address\":{\"city\":\"Winterfell\"}}";

    private static final String CITIES_JSON =
            "{\"cities\" : [\"Warsaw\", \"Kiev\", \"Oslo\", \"Stockholm\", \"Lublin\", \"Cracow\"]}";

    private JsonObject peopleObject;
    private JsonObject personObject;

    @Before
    public void init() {
        this.personObject = getMapper().fromJson(PERSON_JSON, JsonObject.class);
        this.peopleObject = getMapper().fromJson(PEOPLE_JSON, JsonObject.class);
    }

    @Test
    public void shouldFetchArrayOfPrimitives_WhenNoneCriterionApplied() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people.address.city";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonArray()).isTrue();
        assertThat(result.getAsJsonArray().size()).isEqualTo(2);
    }

    @Test
    public void shouldFindElement_WhenIndexOfArrayIsGiven() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people[0].address.city";
        final String expectedCity = "Castle Black";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsString()).isEqualTo(expectedCity);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowException_WhenIndexOutOfArray() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people[10].address.city";

        // when
        Jigson.from(peopleObject).withContext(context).parse(query);
    }

    @Test
    public void shouldTakeElements_WhenStartAndEndSliceGiven() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people[0:2]";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonArray()).isTrue();
        assertThat(result.getAsJsonArray().size()).isEqualTo(2);
    }

    @Test
    public void shouldTakeElements_WhenStartAndStepIsGiven() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final JsonObject citiesObject = getMapper().fromJson(CITIES_JSON, JsonObject.class);
        final String query = "@cities[1::2]";

        // when
        final JsonElement result = Jigson.from(citiesObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonArray()).isTrue();
        assertThat(result.getAsJsonArray().size()).isEqualTo(3);
    }

    @Test
    public void shouldSumAges_WhenTheyAreNumericValues() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people.age.sum()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(45);
    }

    @Test
    public void shouldCountAges_WhenTheyAreAggregatedInArray() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people.age.count()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(2);
    }

    @Test
    public void shouldGetAverageAge_WhenTheyAgesAreAggregatedInArray() {

        // given
        final Context context =
                Context.newContext()
                        .filters().arrays().keepMatchingAndPrimitives()
                        .numbers().withPrecisionAnd(2).withRoundingMode(BigDecimal.ROUND_HALF_UP);
        final String query = "@people.age.avg()";
        final BigDecimal expectedAvg = new BigDecimal("22.50");

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsBigDecimal()).isEqualTo(expectedAvg);
    }

    @Test
    public void shouldSumAges_WhenCriterionIsApplied() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(firstName=Sansa&&age!=25).age.sum()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(20);
    }

    @Test
    public void shouldCountAges_WhenCriterionIsApplied() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(firstName=John&&age>20).age.count()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(1);
    }

    @Test
    public void shouldFindMinAge_WhenCriterionIsApplied() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(age>10).age.min()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(20);
    }

    @Test
    public void shouldFindMaxAge_WhenCriterionIsApplied() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(age<=100).age.max()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(25);
    }

    @Test(expected = IllegalJsonElementException.class)
    public void shouldProduceException_WhenExpectingMaxButNoneElementMeetsCondition() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(age>=100).age.max()";

        // when
        Jigson.from(peopleObject).withContext(context).parse(query);
    }

    @Test(expected = IllegalJsonElementException.class)
    public void shouldProduceException_WhenExpectingMinButNoneElementMeetsCondition() {

        // given
        final Context context = Context.newContext().filters().arrays().keepMatchingAndPrimitives();
        final String query = "@people(age>=100).age.min()";

        // when
        Jigson.from(peopleObject).withContext(context).parse(query);
    }

    @Test
    public void shouldFindLength_WhenJsonElementIsArray() {

        // given
        final Context context = Context.newContext().filters().arrays().onlyMatching();
        final String query = "@people(firstName=Sansa).age.length()";

        // when
        final JsonElement result = Jigson.from(peopleObject).withContext(context).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(1);
    }

    @Test
    public void shouldFindLength_WhenJsonElementIsNumber() {

        // given
        final String query = "@age.length()";

        // when
        final JsonElement result = Jigson.from(personObject).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(2);
    }

    @Test
    public void shouldFindLength_WhenJsonElementIsString() {

        // given
        final String query = "@address.city.length()";

        // when
        final JsonElement result = Jigson.from(personObject).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsInt()).isEqualTo(10);
    }

    @Test
    public void shouldParseExpression_WhenComposedCorrectly() {

        // given
        final String query = "?people.size() > 5";

        // when
        final JsonElement result = Jigson.from(peopleObject).parse(query);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isJsonPrimitive()).isTrue();
        assertThat(result.getAsJsonPrimitive().getAsBoolean()).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhileParsing_WhenRightOperandIsNotNumeric() {

        // given
        final String query = "?people.size() > XXX";

        // when
        Jigson.from(peopleObject).parse(query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhileParsing_WhenLeftOperandIsNotNumeric() {

        // given
        final String query = "?people(firstName=John).lastName > 5";
        final Context context = Context.newContext().filters().arrays().onlyMatching();

        // when
        Jigson.from(peopleObject).withContext(context).parse(query);
    }

    @Test(expected = IllegalQueryException.class)
    public void shouldThrowExceptionWhileParsing_WhenComparisonOperatorNotFound() {

        // given
        final String query = "?people.size()";

        // when
        Jigson.from(peopleObject).parse(query);
    }
}
