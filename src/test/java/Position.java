import java.util.Arrays;
import java.util.stream.Stream;

public enum Position {
    TOP_LEFT, TOP_CENTER, TOP_RIGHT,
    MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;

    static Stream<Position> allPositionsExcept(Position position) {
        return allPositions().filter(x -> !x.equals(position));
    }

    private static Stream<Position> allPositions() {
        return Arrays.stream(values());
    }
}
