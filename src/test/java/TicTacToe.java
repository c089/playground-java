public class TicTacToe {
    private Player currentPlayer = Player.X;
    private Field markedField;

    record Field(Position position, FieldState state) {}
    public Field fieldAt(Position position) {
        if (markedField.position() == position) {
            return markedField;
        }
        return new Field(position, new FieldState.EmptyField());

    }

    public TurnResult markField(Position position) {
        if (markedField != null && markedField.position == position) {
            return new FieldAlreadyMarked(markedField);
        }

        markedField = new Field(position, FieldState.markedBy(currentPlayer));
        currentPlayer = currentPlayer.nextPlayer();
        return new FieldMarked(markedField);
    }

}
