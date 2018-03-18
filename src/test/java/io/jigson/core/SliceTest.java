package io.jigson.core;

import io.jigson.core.flow.Slice;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND;

public class SliceTest {

    @Test
    public void shouldCreateSlice_WhenStartAndEndGiven() {

        // given
        final String index = "1:2";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(1);
        assertThat(slice.getEndIndex()).isEqualTo(2);
    }

    @Test
    public void shouldCreateSlice_WhenStartAndEndAndStepGiven() {

        // given
        final String index = "1:3:2";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(1);
        assertThat(slice.getEndIndex()).isEqualTo(3);
        assertThat(slice.getStep()).isEqualTo(2);
    }

    @Test
    public void shouldCreateSlice_WhenOnlyStartGiven() {

        // given
        final String index = "1::";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(1);
        assertThat(slice.getEndIndex()).isEqualTo(INDEX_NOT_FOUND);
        assertThat(slice.getStep()).isEqualTo(1);
    }

    @Test
    public void shouldCreateSlice_WhenOnlyEndGiven() {

        // given
        final String index = ":3";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(0);
        assertThat(slice.getEndIndex()).isEqualTo(3);
        assertThat(slice.getStep()).isEqualTo(1);
    }

    @Test
    public void shouldCreateSlice_WhenOnlyStepGiven() {

        // given
        final String index = "::2";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(0);
        assertThat(slice.getEndIndex()).isEqualTo(INDEX_NOT_FOUND);
        assertThat(slice.getStep()).isEqualTo(2);
    }

    @Test
    public void shouldCreateSlice_WhenOnlyFirstColonGiven() {

        // given
        final String index = ":";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(0);
        assertThat(slice.getEndIndex()).isEqualTo(INDEX_NOT_FOUND);
        assertThat(slice.getStep()).isEqualTo(1);
    }

    @Test
    public void shouldCreateSlice_WhenBothColonsGiven() {

        // given
        final String index = "::";

        // when
        final Slice slice = Slice.from(index);

        // then
        assertThat(slice).isNotNull();
        assertThat(slice.getStartIndex()).isEqualTo(0);
        assertThat(slice.getEndIndex()).isEqualTo(INDEX_NOT_FOUND);
        assertThat(slice.getStep()).isEqualTo(1);
    }
}
