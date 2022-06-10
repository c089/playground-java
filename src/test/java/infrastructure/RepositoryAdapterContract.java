package infrastructure;

import domain.Server;
import domain.ServerID;
import domain.Volume;
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

    @Test
    void canCreateVolumes() {
        final ServersRepository repository = createRepository();
        Volume volume = repository.createVolume();
        assertThat(repository.findVolumeById(volume.id())).hasValue(volume);
    }

    @Test
    void givenANewServerWhenIAttachVolumesThenTheyAreFound() {
        final ServersRepository repository = createRepository();
        final Server server = repository.createServer();
        final Volume volume1 = repository.createVolume();
        final Volume volume2 = repository.createVolume();

        repository.attachVolume(server.id(), volume1.id());
        repository.attachVolume(server.id(), volume2.id());

        assertThat(repository.findVolumesAttachedTo(server.id())).containsExactlyInAnyOrder(volume1, volume2);
    }

    protected abstract ServersRepository createRepository();
}
