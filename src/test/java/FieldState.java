sealed interface FieldState permits FieldState.EmptyField, FieldState.MarkedBy {
    record EmptyField() implements FieldState {
    }

    record MarkedBy(Player x) implements FieldState {
    }
    static FieldState markedBy(Player markedBy) { return new MarkedBy(markedBy); }
    static FieldState emptyField() { return new EmptyField(); }
}
