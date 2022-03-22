package domain;

public interface TicTacToeGamesRepository {
    int numberOfGamesWonBy(Player x);

    void recordResult(GameWonBy gameWonBy);
}
