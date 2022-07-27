import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class BowlingGame {
    private final Map<Integer, Frame> frames;

    public BowlingGame() {
        frames = new HashMap<>();
        frames.put(1, new Frame.FrameWithNoRolls());
    }

    public Score score() {
        System.out.println(frames);
        Score score = new Score(0);
        for (Frame frame : frames.values()) {
            score = score.add(frame.score());
        }
        return score;
    }

    public void roll(Roll roll) {
        if (noMoreFramesAreAcceptingRolls() && noFrameIsAcceptingABonus()) {
            throw new IllegalStateException("Game has ended.");
        }
        recordPotentialBonusForEarlierFrames(roll);
        if (firstOpenFrame().isPresent()) {
            recordRollForCurrentFrame(roll);
        }
    }

    private boolean noFrameIsAcceptingABonus() {
        return firstFrameAcceptingBonus().isEmpty();
    }

    private boolean noMoreFramesAreAcceptingRolls() {
        return frames.size() == 10 && firstOpenFrame().isEmpty();
    }

    private Optional<Frame> firstFrameAcceptingBonus() {
        return frames.values().stream().filter(Frame::acceptsBonus).findFirst();
    }

    private void recordRollForCurrentFrame(Roll roll) {
        final var firstOpenFrame =
                firstOpenFrame().get();
        final var currentFrame = firstOpenFrame.getValue();
        final var currentFrameNumber = firstOpenFrame.getKey();

        Frame updatedCurrentFrame = currentFrame.roll(roll);
        frames.put(currentFrameNumber, updatedCurrentFrame);
        if (updatedCurrentFrame.isClosed() && currentFrameNumber < 10) {
            frames.put(currentFrameNumber + 1, new Frame.FrameWithNoRolls());
        }
    }

    private Optional<Map.Entry<Integer, Frame>> firstOpenFrame() {
        return frames.entrySet().stream().filter(x -> x.getValue().isOpen()).findFirst();
    }

    private void recordPotentialBonusForEarlierFrames(Roll roll) {
        frames.forEach((key, value) -> {
            if (value.acceptsBonus())
                frames.put(key, value.roll(roll));
        });
    }

}