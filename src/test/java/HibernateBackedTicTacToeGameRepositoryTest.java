import domain.TicTacToeGamesRepository;
import infrastructure.HibernateBackedTicTacToeGamesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateBackedTicTacToeGameRepositoryTest extends TicTacToeGamesRepositoryContract {
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setUp() {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2")
                .withDatabaseName("recipes")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
        System.setProperty("db.port", postgreSQLContainer.getFirstMappedPort().toString());
        entityManagerFactory = Persistence.createEntityManagerFactory("tic-tac-toe");
    }

    @Override
    protected TicTacToeGamesRepository repository() {
        return new HibernateBackedTicTacToeGamesRepository(entityManagerFactory);
    }

}
