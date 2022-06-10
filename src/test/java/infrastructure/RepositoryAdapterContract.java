package infrastructure;

import domain.Server;
import domain.ServerID;
import org.junit.jupiter.api.Test;
import ports.driven.ServersRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class RepositoryAdapterContract {
    @Test
    void givenANewServerItHasNoVolumes() {
        final ServersRepository repositoryAdapter = createRepository();
        final Server server = repositoryAdapter.createServer();
        assertThat(repositoryAdapter.findVolumesAttachedTo(server.id())).isEmpty();
    }


    @Test
    void givenANonExistingServerItCanNotBeFoundByID() {
        final ServersRepository repositoryAdapter = createRepository();
        final ServerID nonExistingServerID = new ServerID(UUID.randomUUID());
        assertThat(repositoryAdapter.findServerById(nonExistingServerID)).isNotPresent();
    }

    @Test
    void givenANewServerItCanBeFoundByID() {
        final ServersRepository repositoryAdapter = createRepository();
        final Server server = repositoryAdapter.createServer();
        assertThat(repositoryAdapter.findServerById(server.id())).hasValue(server);
    }

    protected abstract ServersRepository createRepository();
}
