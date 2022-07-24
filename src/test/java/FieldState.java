sealed interface FieldState {
    boolean ownedBy(Player currentPlayer);

    record EmptyField() implements FieldState {
        @Override
        public boolean ownedBy(Player currentPlayer) {
            return false;
        }
    }

    record MarkedBy(Player owner) implements FieldState {
        @Override
        public boolean ownedBy(Player player) {
            return owner.equals(player);
        }
    }

    static FieldState markedBy(Player markedBy) {
        return new MarkedBy(markedBy);
    }

    static FieldState emptyField() {
        return new EmptyField();
    }
}
