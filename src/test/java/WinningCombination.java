import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record WinningCombination(Position p1, Position p2, Position p3) {
    private static final WinningCombination TOP_ROW = new WinningCombination(Position.TOP_LEFT, Position.TOP_CENTER, Position.TOP_RIGHT);
    private static final WinningCombination MIDDLE_ROW = new WinningCombination(Position.MIDDLE_LEFT, Position.MIDDLE, Position.MIDDLE_RIGHT);
    private static final WinningCombination BOTTOM_ROW = new WinningCombination(Position.BOTTOM_LEFT, Position.BOTTOM_CENTER, Position.BOTTOM_RIGHT);
    private static final WinningCombination LEFT_COLUMN = new WinningCombination(Position.TOP_LEFT, Position.MIDDLE_LEFT, Position.BOTTOM_LEFT);
    private static final WinningCombination CENTER_COLUMN = new WinningCombination(Position.TOP_CENTER, Position.MIDDLE, Position.BOTTOM_CENTER);
    private static final WinningCombination RIGHT_COLUMN = new WinningCombination(Position.TOP_RIGHT, Position.MIDDLE_RIGHT, Position.BOTTOM_RIGHT);
    private static final WinningCombination TOP_LEFT_TO_BOTTOM_RIGHT_DIAGONAL = new WinningCombination(Position.TOP_LEFT, Position.MIDDLE, Position.BOTTOM_RIGHT);
    private static final WinningCombination BOTTOM_LEFT_TO_TOP_RIGHT_DIAGONAL = new WinningCombination(Position.BOTTOM_LEFT, Position.MIDDLE, Position.TOP_RIGHT);
    public static final Set<WinningCombination> WINNING_COMBINATIONS = Set.of(
            TOP_ROW,
            MIDDLE_ROW,
            BOTTOM_ROW,
            LEFT_COLUMN,
            CENTER_COLUMN,
            RIGHT_COLUMN,
            TOP_LEFT_TO_BOTTOM_RIGHT_DIAGONAL,
            BOTTOM_LEFT_TO_TOP_RIGHT_DIAGONAL
    );

    static boolean isWinningCombination(Position p1, Position p2, Position p3) {
        Set<Position> potentialWinningCombination = Set.of(p1, p2, p3);
        return WINNING_COMBINATIONS.stream().noneMatch(combination ->
                combination.stream().collect(Collectors.toSet())
                        .equals(potentialWinningCombination));
    }

    Stream<Position> stream() {
        return Stream.of(p1, p2, p3);
    }

}
