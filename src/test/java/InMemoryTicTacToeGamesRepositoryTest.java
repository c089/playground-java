import domain.GameWonBy;
import domain.Player;
import domain.TicTacToeGamesRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTicTacToeGamesRepositoryTest extends TicTacToeGamesRepositoryContract {
    @Override
    protected TicTacToeGamesRepository repository() {
        return new InMemoryTicTacToGamesRepository();
    }

    private static class InMemoryTicTacToGamesRepository implements TicTacToeGamesRepository {
        private final List<GameWonBy> gameWonBys = new ArrayList<>();

        @Override
        public int numberOfGamesWonBy(Player x) {
            return gameWonBys.stream().filter(p -> x.equals(p.player())).toList().size();
        }

        @Override
        public void recordResult(GameWonBy gameWonBy) {
            this.gameWonBys.add(gameWonBy);
        }
    }
}
