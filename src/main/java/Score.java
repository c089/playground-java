record Score(int score) {
    public Score add(Score that) {
        return new Score(this.score + that.score);
    }
}
