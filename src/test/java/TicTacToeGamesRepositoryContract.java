import domain.GameWonBy;
import domain.Player;
import domain.TicTacToeGamesRepository;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public abstract class TicTacToeGamesRepositoryContract {
    protected abstract TicTacToeGamesRepository repository();

    @Test
    void givenNoGamesHabeBeenPlayerReturns0GamesWon() {
        TicTacToeGamesRepository repository = repository();
        assertThat(repository.numberOfGamesWonBy(Player.X), is(equalTo(0)));
    }

    @Test
    void givenAGameWonByXCountsOneGameWOn() {
        TicTacToeGamesRepository repository = repository();
        repository.recordResult(new GameWonBy(Player.X));
        assertThat(repository.numberOfGamesWonBy(Player.X), is(equalTo(1)));
    }

    @Test
    void givenManyGamesWonByXCountsGamesWon() {
        TicTacToeGamesRepository repository = repository();

        repository.recordResult(new GameWonBy(Player.X));
        repository.recordResult(new GameWonBy(Player.X));
        repository.recordResult(new GameWonBy(Player.X));

        assertThat(repository.numberOfGamesWonBy(Player.X), is(equalTo(3)));
    }

    @Test
    void givenManyGamesWonByOCountsGamesWon() {
        TicTacToeGamesRepository repository = repository();

        repository.recordResult(new GameWonBy(Player.O));
        repository.recordResult(new GameWonBy(Player.O));
        repository.recordResult(new GameWonBy(Player.O));

        assertThat(repository.numberOfGamesWonBy(Player.O), is(equalTo(3)));
    }

    @Test
    void givenSomeGamesWonByEachPlayerCountsThemCorrectly() {
        TicTacToeGamesRepository repository = repository();

        repository.recordResult(new GameWonBy(Player.X));
        repository.recordResult(new GameWonBy(Player.O));
        repository.recordResult(new GameWonBy(Player.X));
        repository.recordResult(new GameWonBy(Player.O));
        repository.recordResult(new GameWonBy(Player.X));

        assertThat(repository.numberOfGamesWonBy(Player.X), is(equalTo(3)));
        assertThat(repository.numberOfGamesWonBy(Player.O), is(equalTo(2)));
    }
}
