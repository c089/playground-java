import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BowlingGameTest {
    @Test
    void beforeTheFirstRollTheScoreIs0() {
        BowlingGame bowlingGame = new BowlingGame();
        assertThat(bowlingGame.score()).isEqualTo(new Score(0));
    }

    @Test
    void rollingOnePinScoreIs1() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(1));
        assertThat(bowlingGame.score()).isEqualTo(new Score(1));
    }

    @Test
    void rollingTwoPinsScoreIs2() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(2));
        assertThat(bowlingGame.score()).isEqualTo(new Score(2));
    }

    @Test
    void rollingTwoTimesTwoPinsScoreIs4() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(2));
        assertThat(bowlingGame.score()).isEqualTo(new Score(4));
    }

    @Test
    void rollingASpareBeforeTheBonusRollIsRolledScoreIsSumOfRolls() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(8));
        bowlingGame.roll(Roll.of(2));
        assertThat(bowlingGame.score()).isEqualTo(new Score(10));
    }

    @Test
    void rollingThreeTimesWithoutASpareTheScoreIsTheSumOfRolls() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(4));
        bowlingGame.roll(Roll.of(6));
        assertThat(bowlingGame.score()).isEqualTo(new Score(12));
    }

    @Test
    void rollingFourFramesWithSomeSpares() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(8));

        bowlingGame.roll(Roll.of(6));
        bowlingGame.roll(Roll.of(2));

        bowlingGame.roll(Roll.of(3));
        bowlingGame.roll(Roll.of(7));

        bowlingGame.roll(Roll.of(1));
        bowlingGame.roll(Roll.of(5));

        assertThat(bowlingGame.score()).isEqualTo(new Score((2 + 8 + 6) + (6 + 2) + (3 + 7 + 1) + (1 + 5)));
    }

    @Test
    void rollingThreeFramesWithoutASpare() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(2));

        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(2));

        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(2));

        assertThat(bowlingGame.score()).isEqualTo(new Score(12));
    }

    @Test
    void ifFirstFrameIsSpareThenFirstRollOfSecondFrameGetsAddedAsABonus() {
        BowlingGame bowlingGame = new BowlingGame();
        int frame1Roll1 = 8;
        int frame1Roll2 = 2;
        int frame2Roll1 = 5;

        bowlingGame.roll(Roll.of(frame1Roll1));
        bowlingGame.roll(Roll.of(frame1Roll2));
        bowlingGame.roll(Roll.of(frame2Roll1));

        int frame1Score = frame1Roll1 + frame1Roll2 + frame2Roll1;
        int frame2Score = frame2Roll1;
        assertThat(bowlingGame.score()).isEqualTo(new Score(frame1Score + frame2Score));
    }

    @Test
    void ifFirstFrameIsSpareThenFirstRollOfSecondFrameGetsAddedAsABonus_foo() {
        BowlingGame bowlingGame = new BowlingGame();
        int frame1Roll1 = 6;
        int frame1Roll2 = 4;
        int frame2Roll1 = 3;

        bowlingGame.roll(Roll.of(frame1Roll1));
        bowlingGame.roll(Roll.of(frame1Roll2));
        bowlingGame.roll(Roll.of(frame2Roll1));

        int frame1Score = frame1Roll1 + frame1Roll2 + frame2Roll1;
        int frame2Score = frame2Roll1;
        assertThat(bowlingGame.score()).isEqualTo(new Score((frame1Score + frame2Score)));
    }

    @Test
    void givenAStrikeInFrameOneFollowedByTwoRegularRollsAddsBothAsBonus() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(10));

        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(4));

        assertThat(bowlingGame.score()).isEqualTo(new Score((10 + 2 + 4) + 2 + 4));
    }

    @Test
    void givenTwoStrikesInARowTheFirstReceivesBonusFromTheSecondsStrikeAndTheFirstRollAfter() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(Roll.of(10));

        bowlingGame.roll(Roll.of(10));

        bowlingGame.roll(Roll.of(2));

        assertThat(bowlingGame.score()).isEqualTo(new Score((10 + 10 + 2) + (10 + 2) + (2)));
    }

    @Test
    void scoresAFullGameOfRegularFrames() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 20, 2);
        assertThat(bowlingGame.score()).isEqualTo(new Score(20 * 2));
    }

    @Test
    void throwsWhenTryingToAddMoreRollsToAFinishedGame() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 20, 2);

        assertThatThrownBy(
                () -> bowlingGame.roll(Roll.of(5)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Game has ended.");
    }

    @Test
    void whenRollingASpareInLastFrameCanRollOneMoreAsBonus() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 18, 2);

        bowlingGame.roll(Roll.of(8));
        bowlingGame.roll(Roll.of(2));

        bowlingGame.roll(Roll.of(3));

        assertThat(bowlingGame.score()).isEqualTo(new Score((18 * 2) + (8 + 2 + 3)));
    }

    @Test
    void whenRollingAStrikeInLastFrameCanRollTwoMoreAsBonus() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 18, 2);

        bowlingGame.roll(Roll.of(10));

        bowlingGame.roll(Roll.of(2));
        bowlingGame.roll(Roll.of(3));

        assertThat(bowlingGame.score()).isEqualTo(new Score((18 * 2) + (10 + 2 + 3)));
    }

    private void rollMany(BowlingGame bowlingGame, int numberOfRolls, int numberOfPins) {
        IntStream.iterate(numberOfPins, i -> i).limit(numberOfRolls).forEach(i ->
                bowlingGame.roll(Roll.of(i)));
    }
}
