import org.jetbrains.annotations.NotNull;

public enum Player {
    O, X;

    @NotNull Player nextPlayer() {
        return this == X ? O : X;
    }
}
