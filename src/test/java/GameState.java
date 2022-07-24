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
                case MarkFieldResult.FieldMarked fieldMarked -> {
                    Board board = board().mark(position, fieldMarked.field().state());
                    if (WinningCombination.WINNING_COMBINATIONS.stream().anyMatch(winningCombination -> winsByCombination(winningCombination, currentPlayer()))) {
                        yield new GameWonBy(player, board);
                    } else {
                        yield new GameInProgress(player.nextPlayer(), board);
                    }
                }
            };
        }

        boolean winsByCombination(WinningCombination winningCombination, Player p) {
            return winningCombination.stream().allMatch(position -> fieldAt(position).ownedBy(p));
        }
    }
}
