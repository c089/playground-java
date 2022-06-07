package infrastructure.springdata;

import infrastructure.RepositoryAdapterContract;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import ports.driven.ServersRepository;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class SpringDataRepositoryAdapterTest extends RepositoryAdapterContract {

    @Override
    protected ServersRepository createRepository() {
        setupDatabase();

        final JpaRepositoryFactory factory = createRepositoryFactory();
        SpringBasedServersRepository repository = factory.getRepository(SpringBasedServersRepository.class);

        return new SpringDataServerRepositoryAdapter(repository);
    }

    private JpaRepositoryFactory createRepositoryFactory() {
        final EntityManager entityManager = Persistence.createEntityManagerFactory("ports-and-adapters").createEntityManager();
        return new JpaRepositoryFactory(entityManager);
    }

    private void setupDatabase() {
        var postgreSQLContainer = new PostgreSQLContainer("postgres")
                .withDatabaseName("servers")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
        System.setProperty("db.port", postgreSQLContainer.getFirstMappedPort().toString());
    }

}
