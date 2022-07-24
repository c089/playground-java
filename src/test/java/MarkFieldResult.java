public sealed interface MarkFieldResult {
    record FieldMarked(Field field) implements MarkFieldResult {
    }

    record FieldAlreadyMarked(Field field) implements MarkFieldResult {
    }

}
