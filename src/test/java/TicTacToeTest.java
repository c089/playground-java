import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TicTacToeTest {
    @Property
    void givenANewGameXPlaysFirstAndMarksAField(@ForAll Position position) {
        TicTacToe game = new TicTacToe();

        GameState result = game.markField(position);

        assertThat(game.fieldAt(position))
                .isEqualTo(new Field(position, FieldState.markedBy(Player.X)));
        assertThat(result).isEqualTo(
                new GameState.GameInProgress(Player.O, new Board(Map.of(position, new FieldState.MarkedBy(Player.X)))));
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
        GameState result = game.markField(positionChosenByO);

        assertThat(game.fieldAt(positionChosenByO).state()).isEqualTo(FieldState.markedBy(Player.O));
        assertThat(result)
                .isEqualTo(new GameState.GameInProgress(Player.O.nextPlayer(),
                        new Board(Map.of(
                                positionChosenByX, FieldState.markedBy(Player.X),
                                positionChosenByO, FieldState.markedBy(Player.O)
                        ))));
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
        GameState turn = game.markField(Position.TOP_LEFT);
        assertThat(turn).isEqualTo(new GameState.GameInProgress(Player.O, new Board(Map.of(Position.TOP_LEFT, FieldState.markedBy(Player.X)))));
        assertThat(game.fieldAt(Position.TOP_LEFT).state()).isEqualTo(FieldState.markedBy(Player.X));
    }

    @Property
    void givenANewGameWhenXPlaysAWinningCombinationThenTheyWinTheGame(
            @ForAll(supplier = WinningCombinationArbitrary.class) WinningCombination winningCombination) {
        TicTacToe game = new TicTacToe();
        List<Position> otherFields = Position.allPositionsExcept(winningCombination.p1(), winningCombination.p2(), winningCombination.p3());

        game.markField(winningCombination.p1());
        game.markField(otherFields.get(0));
        game.markField(winningCombination.p2());
        game.markField(otherFields.get(1));
        GameState turn = game.markField(winningCombination.p3());

        assertThat(turn).isEqualTo(new GameState.GameWonBy(Player.X, turn.board()));
    }

    @Property
    void givenANewGameWhenOPlaysAWinningCombinationThenTheyWinTheGame(
            @ForAll(supplier = WinningCombinationArbitrary.class) WinningCombination winningCombination) {
        TicTacToe game = new TicTacToe();

        List<Position> losingCombination = losingCombination(winningCombination).sample();

        game.markField(losingCombination.get(0));
        game.markField(winningCombination.p1());
        game.markField(losingCombination.get(1));
        game.markField(winningCombination.p2());
        game.markField(losingCombination.get(2));

        GameState turn = game.markField(winningCombination.p3());

        assertThat(turn).isEqualTo(new GameState.GameWonBy(Player.O, turn.board()));
    }

    @Example
    void cannotPlayOnGameThatHasEnded(
            @ForAll(supplier = WinningCombinationArbitrary.class) WinningCombination winningCombination) {
        TicTacToe game = new TicTacToe();

        List<Position> losingCombination = losingCombination(winningCombination).sample();

        game.markField(losingCombination.get(0));
        game.markField(winningCombination.p1());
        game.markField(losingCombination.get(1));
        game.markField(winningCombination.p2());
        game.markField(losingCombination.get(2));
        game.markField(winningCombination.p3());

        Position remainingField = Position.allPositionsExcept(Stream.concat(winningCombination.stream(), losingCombination.stream()).toList().toArray(new Position[0])).get(0);


        assertThatThrownBy(() -> game.markField(remainingField))
                .isInstanceOf(IllegalStateException.class).hasMessage("Game has ended.");
    }

    private Arbitrary<List<Position>> losingCombination(WinningCombination winningCombination) {
        return Arbitraries
                .of(Position.allPositionsExcept(winningCombination.p1(), winningCombination.p2(), winningCombination.p3()))
                .list()
                .uniqueElements()
                .ofSize(3)
                .filter(ps -> WinningCombination.isWinningCombination(ps.get(0), ps.get(1), ps.get(2)));
    }


    private static class WinningCombinationArbitrary implements ArbitrarySupplier<WinningCombination> {
        @Override
        public Arbitrary<WinningCombination> get() {
            return Arbitraries.of(WinningCombination.WINNING_COMBINATIONS);
        }
    }

}
