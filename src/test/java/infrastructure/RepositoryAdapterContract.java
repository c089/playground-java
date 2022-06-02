package infrastructure;

import domain.Server;
import org.junit.jupiter.api.Test;
import ports.driven.ServersRepository;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class RepositoryAdapterContract {
    @Test
    void givenANewServerItHasNoVolumes() {
        final ServersRepository repositoryAdapter = createRepository();
        final Server server = repositoryAdapter.createServer();
        assertThat(repositoryAdapter.findVolumesAttachedTo(server.id())).isEmpty();
    }

    protected abstract ServersRepository createRepository();
}
