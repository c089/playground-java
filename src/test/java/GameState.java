sealed interface GameState {
    static GameState.GameInProgress startGame() {
        return new GameInProgress(Player.X, new Board());
    }

    default Field fieldAt(Position position) {
        return board().fieldAt(position);
    }

    Board board();

    record GameWonBy(Player winner, Board board) implements GameState {
    }

    record GameInProgress(Player currentPlayer, Board board) implements GameState {
        GameState markField(Position position, Player player) {
            return switch (fieldAt(position).mark(player)) {
                case MarkFieldResult.FieldAlreadyMarked ignored ->
                        this; // TODO signal error to outside? return tuple (MarkFieldResult, GameState)?
                case MarkFieldResult.FieldMarked fieldMarked -> nextState(position, player, fieldMarked);
            };
        }

        private GameState nextState(Position position, Player player, MarkFieldResult.FieldMarked fieldMarked) {
            Board board = board().mark(position, fieldMarked.field().state());

            if (hasWon(player)) {
                return new GameWonBy(player, board);
            } else {
                return new GameInProgress(player.nextPlayer(), board);
            }
        }

        private boolean hasWon(Player p) {
            return WinningCombination.hasMarkedAWinningCombination(board().positionsMarkedBy(p));
        }

    }
}
