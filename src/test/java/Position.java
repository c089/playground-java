import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Position {
    TOP_LEFT, TOP_CENTER, TOP_RIGHT,
    MIDDLE_LEFT, MIDDLE, MIDDLE_RIGHT,
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT;

    static Stream<Position> allPositionsExcept(Position position) {
        return allPositions().filter(x -> !x.equals(position));
    }

    public static List<Position> allPositionsExcept(Position... except) {
        return allPositions().filter(p -> !Arrays.asList(except).contains(p)).collect(Collectors.toList());
    }

    private static Stream<Position> allPositions() {
        return Arrays.stream(values());
    }

}
