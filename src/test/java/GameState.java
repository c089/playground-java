sealed interface GameState {
    record GameWonBy(Player winner) implements GameState {
    }

    record GameInProgress(MarkFieldResult lastTurn) implements GameState {
    }
}
