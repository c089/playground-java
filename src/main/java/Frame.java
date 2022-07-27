sealed interface Frame {
    Score score();

    Frame roll(Roll roll);

    boolean isOpen();

    default boolean isClosed() {
        return !isOpen();
    }

    default boolean acceptsBonus() {
        return false;
    }

    record FrameWithNoRolls() implements Frame {
        @Override
        public boolean isOpen() {
            return true;
        }

        @Override
        public Frame roll(Roll roll) {
            if (roll.pins().score() == 10) return new StrikeFrame();
            return new FrameWithOneRoll(roll);
        }

        @Override
        public Score score() {
            return new Score(0);
        }

    }

    record StrikeFrame() implements Frame {
        @Override
        public Score score() {
            return new Score(10);
        }

        @Override
        public Frame roll(Roll roll) {
            return new StrikeFrameWithFirstBonus(roll);
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean acceptsBonus() {
            return true;
        }

    }

    record StrikeFrameWithFirstBonus(Roll bonusRoll1) implements Frame {
        @Override
        public Score score() {
            return new Score(10).add(bonusRoll1.pins());
        }

        @Override
        public Frame roll(Roll bonusRoll2) {
            return new StrikeFrameWithFullBonus(bonusRoll1, bonusRoll2);
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean acceptsBonus() {
            return true;
        }
    }

    record StrikeFrameWithFullBonus(Roll bonus1, Roll bonus2) implements Frame {
        @Override
        public Score score() {
            return new Score(10).add(bonus1.pins()).add(bonus2.pins());
        }

        @Override
        public Frame roll(Roll roll) {
            return this;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

    }

    record FrameWithOneRoll(Roll roll1) implements Frame {
        @Override
        public Frame roll(Roll roll) {
            return new FrameWithTwoRolls(roll1, roll);
        }

        @Override
        public Score score() {
            return roll1.pins();
        }

        @Override
        public boolean isOpen() {
            return true;
        }

    }

    record FrameWithTwoRolls(Roll roll1, Roll roll2) implements Frame {
        @Override
        public Frame roll(Roll roll) {
            if (isSpare()) {
                return new SpareFrameWithBonus(roll1, roll2, roll);
            }
            return this;
        }

        @Override
        public Score score() {
            return roll1.pins().add(roll2.pins());
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public boolean acceptsBonus() {
            return isSpare();
        }

        private boolean isSpare() {
            return roll1.pins().add(roll2.pins()).score() == 10;
        }

    }

    record SpareFrameWithBonus(Roll roll1, Roll roll2, Roll bonusRoll) implements Frame {
        @Override
        public Frame roll(Roll roll) {
            return this;
        }

        public Score score() {
            return roll1.pins().add(roll2.pins()).add(bonusRoll.pins());
        }

        @Override
        public boolean isOpen() {
            return false;
        }

    }
}