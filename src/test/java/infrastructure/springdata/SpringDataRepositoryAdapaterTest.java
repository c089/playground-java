package infrastructure.springdata;

import infrastructure.RepositoryAdapaterContract;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import ports.driven.ServersRepository;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class SpringDataRepositoryAdapaterTest extends RepositoryAdapaterContract {

    @Override
    protected ServersRepository createRepository() {
        setupDatabase();

        final JpaRepositoryFactory factory = createRepositoryFactory();
        SpringBasedServersRepository repository = factory.getRepository(SpringBasedServersRepository.class);

        final SpringDataServerRepositoryAdapter repositoryAdapter = new SpringDataServerRepositoryAdapter(repository);
        return repositoryAdapter;
    }

    private JpaRepositoryFactory createRepositoryFactory() {
        final EntityManager entityManager = Persistence.createEntityManagerFactory("ports-and-adapters").createEntityManager();
        final JpaRepositoryFactory factory = new JpaRepositoryFactory(entityManager);
        return factory;
    }

    private void setupDatabase() {
        final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.2")
                .withDatabaseName("servers")
                .withUsername("postgres")
                .withPassword("postgres");
        postgreSQLContainer.start();
        System.setProperty("db.port", postgreSQLContainer.getFirstMappedPort().toString());
    }

}
