public enum Player {
    O, X;

    Player nextPlayer() {
        return this == X ? O : X;
    }
}
