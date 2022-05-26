import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;


public class TicTacToeTest {
    @Property
    void givenANewGameXPlaysFirstAndMarksAField(@ForAll Position position) {
        TicTacToe game = new TicTacToe();

        game.markField(position);

        assertThat(game.fieldAt(position))
                .isEqualTo(new TicTacToe.Field(position, FieldState.markedBy(Player.X)));
    }

    @Property
    void givenANewGameWhenXMarksAFieldAllOtherFieldsAreEmpty(@ForAll Position position) {
        TicTacToe game = new TicTacToe();

        game.markField(position);

        assertThat(Position.allPositionsExcept(position).map(game::fieldAt))
                .allSatisfy(field -> assertThat(field.state()).isEqualTo(FieldState.emptyField()));
    }

}
