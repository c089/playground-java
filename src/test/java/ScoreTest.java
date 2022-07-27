import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScoreTest {
    @Test
    void canAddAScore() {
        assertThat(new Score(0).add(new Score(5))).isEqualTo(new Score(5));
    }

    @Test
    void canAddAScore2() {
        assertThat(new Score(7).add(new Score(2))).isEqualTo(new Score(9));
    }

}
