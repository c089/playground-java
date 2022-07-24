import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicTacToeTest {
    @Property
    void givenANewGameXPlaysFirstAndMarksAField(@ForAll Position position) {
        TicTacToe game = new TicTacToe();

        TurnResult result = game.markField(position);

        assertThat(game.fieldAt(position))
                .isEqualTo(new TicTacToe.Field(position, FieldState.markedBy(Player.X)));
        assertThat(result)
                .isEqualTo(new FieldMarked(new TicTacToe.Field(position, FieldState.markedBy(Player.X))));

    }

    @Property
    void givenANewGameWhenXMarksAFieldAllOtherFieldsAreEmpty(@ForAll Position position) {
        TicTacToe game = new TicTacToe();

        game.markField(position);

        assertThat(Position.allPositionsExcept(position).map(game::fieldAt))
                .allSatisfy(field -> assertThat(field.state()).isEqualTo(FieldState.emptyField()));
    }

    @Test
    void givenXTookTheirTurnItIsPlayerOsTurn() {
        TicTacToe game = new TicTacToe();

        Position positionChosenByX = Position.TOP_LEFT;
        Position positionChosenByO = Position.TOP_RIGHT;

        game.markField(positionChosenByX);
        TurnResult result = game.markField(positionChosenByO);

        assertThat(game.fieldAt(positionChosenByO).state()).isEqualTo(FieldState.markedBy(Player.O));
        assertThat(result)
                .isEqualTo(new FieldMarked(new TicTacToe.Field(positionChosenByO, FieldState.markedBy(Player.O))));
    }

    @Test
    void givenOTookTheirFirstTurnThenItIsPlayerXsTurn() {
        TicTacToe game = new TicTacToe();
        game.markField(Position.TOP_LEFT);
        game.markField(Position.TOP_RIGHT);
        game.markField(Position.BOTTOM_LEFT);
        assertThat(game.fieldAt(Position.BOTTOM_LEFT).state()).isEqualTo(FieldState.markedBy(Player.X));
    }

    @Test
    void givenAFieldHasBeenMarkedItCannotBeMarkedByAnotherPlayer() {
        TicTacToe game = new TicTacToe();
        game.markField(Position.TOP_LEFT);
        TurnResult turn = game.markField(Position.TOP_LEFT);
        assertThat(turn).isEqualTo(new FieldAlreadyMarked(new TicTacToe.Field(Position.TOP_LEFT, FieldState.markedBy(Player.X))));
        assertThat(game.fieldAt(Position.TOP_LEFT).state()).isEqualTo(FieldState.markedBy(Player.X));
    }


}
