public class TicTacToe {
    private Position markedField;

    record Field(Position position, FieldState state) {}
    public Field fieldAt(Position topLeft) {
        /*if (markedField != topLeft)
        return new Field(topLeft, new FieldState.EmptyField());*/
        return new Field(topLeft, new FieldState.MarkedBy(Player.X));
    }

    public void markField(Position position) {
        markedField = position;
    }
}
