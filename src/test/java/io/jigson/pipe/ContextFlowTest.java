package io.jigson.pipe;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ContextFlowTest {

    @Test
    public void shouldWrapFlowWithinContextFlow() {

        // given
        final String value = "abc";
        final String expectedValue = "ABC";
        final UnitaryFlow<String> flow = StringUtils::upperCase;

        // when
        final ContextFlow<String, String> contextFlow = ContextFlow.fromFlow(flow);
        final String actualValue = contextFlow.flow(value);

        // then
        assertThat(contextFlow).isNotNull();
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
