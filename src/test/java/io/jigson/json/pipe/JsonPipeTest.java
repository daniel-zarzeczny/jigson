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

package io.jigson.json.pipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.jigson.core.JigsonConfig;
import io.jigson.core.JigsonConfigHolder;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.jigson.utils.JsonUtils.getMapper;

public class JsonPipeTest {

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
        JigsonConfigHolder.init();
    }

    @Test
    public void shouldMatch_WhenCriterionIsMet() {

        // given
        final String criterion = "firstName=John";

        // when
        final boolean match = JsonPipe.from(jsonObject).match(criterion);

        // then
        assertThat(match).isTrue();
    }

    @Test
    public void shouldNotMatch_WhenCriterionIsNotMet() {

        // given
        final String criterion = "firstName=Johnny";

        // when
        final boolean match = JsonPipe.from(jsonObject).match(criterion);

        // then
        assertThat(match).isFalse();
    }

    @Test
    public void shouldMatch_WhenOneElementMeetsCriterion() {

        // given
        final String criterion = "firstName=John";
        final JigsonConfig config = JigsonConfig.newInstance().filters().arrays().onlyMatching();
        JigsonConfigHolder.set(config);

        // when
        final boolean match = JsonPipe.from(jsonArray).match(criterion);

        // then
        assertThat(match).isTrue();
    }

    @Test
    public void shouldNotMatch_WhenNoneElementMeetsCriterion() {

        // given
        final String criterion = "firstName=Johnny";
        final JigsonConfig config = JigsonConfig.newInstance().filters().arrays().onlyMatching();
        JigsonConfigHolder.set(config);

        // when
        final boolean match = JsonPipe.from(jsonArray).match(criterion);

        // then
        assertThat(match).isFalse();
    }
}
