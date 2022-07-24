import org.jetbrains.annotations.NotNull;

record Field(Position position, FieldState state) {
    @NotNull
    public MarkFieldResult mark(Player markingPlayer) {
        return switch (state) {
            case FieldState.EmptyField a ->
                    new MarkFieldResult.FieldMarked(new Field(position, FieldState.markedBy(markingPlayer)));
            case FieldState.MarkedBy a -> new MarkFieldResult.FieldAlreadyMarked(this);
        };
    }

    boolean ownedBy(Player player) {
        return state.ownedBy(player);
    }


}
