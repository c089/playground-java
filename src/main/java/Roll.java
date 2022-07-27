public record Roll(Score pins) {
    static Roll of(int pins) {
        return new Roll(new Score(pins));
    }
}
