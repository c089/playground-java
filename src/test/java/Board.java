import java.util.HashMap;
import java.util.Map;

public record Board(Map<Position, FieldState> fields) {
    public Board() {
        this(new HashMap<>());
    }

    Board mark(Position position, FieldState state) {
        fields().put(position, state);
        return new Board(fields);
    }

    Field fieldAt(Position position) {
        return new Field(position, this.fields.getOrDefault(position, new FieldState.EmptyField()));
    }
}
