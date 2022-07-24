public class TicTacToe {
    private GameState gameState = GameState.startGame();

    public Field fieldAt(Position position) {
        return gameState.fieldAt(position);
    }

    public GameState markField(Position position) {
        gameState = switch (gameState) {
            case GameState.GameInProgress a -> a.markField(position, a.currentPlayer());
            case GameState.GameWonBy ignored -> throw new IllegalStateException("Game has ended.");
        };
        return gameState;
    }

}
