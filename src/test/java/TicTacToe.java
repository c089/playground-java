import java.util.HashMap;
import java.util.Map;

public class TicTacToe {
    private Player currentPlayer = Player.X;
    private final Map<Position, FieldState> fields = new HashMap<>();

    public Field fieldAt(Position position) {
        return new Field(position, fields.getOrDefault(position, new FieldState.EmptyField()));
    }

    public GameState markField(Position position) {
        GameState gameState = switch (fieldAt(position).mark(this.currentPlayer)) {
            case MarkFieldResult.FieldAlreadyMarked fieldAlreadyMarked ->
                    new GameState.GameInProgress(fieldAlreadyMarked);
            case MarkFieldResult.FieldMarked fieldMarked -> {
                fields.put(position, fieldMarked.field().state());
                if (WinningCombination.WINNING_COMBINATIONS.stream().anyMatch(this::winsByCombination)) {
                    yield new GameState.GameWonBy(currentPlayer);
                } else {
                    currentPlayer = currentPlayer.nextPlayer();
                    yield new GameState.GameInProgress(fieldMarked);
                }
            }
        };
        return gameState;
    }

    private boolean winsByCombination(WinningCombination winningCombination) {
        return winningCombination.stream().allMatch(position -> fieldAt(position).ownedBy(currentPlayer));
    }

}
