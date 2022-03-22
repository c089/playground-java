package infrastructure;

import domain.GameWonBy;
import domain.Player;
import domain.TicTacToeGamesRepository;
import infrastructure.GameWonByEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class HibernateBackedTicTacToeGamesRepository implements TicTacToeGamesRepository {

    private EntityManagerFactory entityManagerFactory;

    public HibernateBackedTicTacToeGamesRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public int numberOfGamesWonBy(Player x) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Long result = entityManager.createQuery(
                        "SELECT COUNT(g) from GameWonByEntity g WHERE g.player = :player", Long.class)
                .setParameter("player", x)
                .getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result.intValue();
    }

    @Override
    public void recordResult(GameWonBy gameWonBy) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new GameWonByEntity(gameWonBy));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
