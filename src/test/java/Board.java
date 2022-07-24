import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Board(Map<Position, FieldState> fields) {
    public Board() {
        this(new HashMap<>());
    }

    Board mark(Position position, FieldState state) {
        // TODO use immutable map
        fields().put(position, state);
        return new Board(fields);
    }

    Field fieldAt(Position position) {
        return new Field(position, this.fields.getOrDefault(position, new FieldState.EmptyField()));
    }

    List<Position> positionsMarkedBy(Player p) {
        return fields().entrySet().stream().filter(x -> x.getValue().ownedBy(p)).map(Map.Entry::getKey).toList();
    }
}
