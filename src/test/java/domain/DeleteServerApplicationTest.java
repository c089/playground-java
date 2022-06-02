package domain;

import infrastructure.InMemoryServersRepositoryAdapter;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import static domain.DeleteServerResponse.DeletionRequestAccepted;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteServerApplicationTest {
    @Test
    public void givenAnExistingServerWhenIDeleteTheServerWithoutVolumesThenOnlyTheServerIsDeleted() {
        final Server server = aServer(List.of(aVolume(), aVolume()));

        DeleteServerResponse result = createService(List.of(server))
                .deleteServer(new DeleteServerRequest(server.id(), KeepAttachedVolumes));

        assertThat(result).isEqualTo(new DeletionRequestAccepted(List.of(server.id())));
    }

    @Test
    void givenAnExistingServerWhenIDeleteItIncludingVolumesTheyAreDeleted() {
        Volume volume1 = aVolume();
        Volume volume2 = aVolume();
        Server server = aServer(List.of(volume1, volume2));
        final List<Server> servers = List.of(server);

        DeleteServerResponse result = createService(servers)
                .deleteServer(new DeleteServerRequest(server.id(), DeleteAttachedVolumes));

        assertThat(result).isEqualTo(new DeletionRequestAccepted(List.of(server.id(), volume1.id(), volume2.id())));
    }

    @Test
    void givenARequestToDeleteANonExistingServerIAmToldTheServerDoesNotExist() {
        final ServerID serverID = new ServerID(UUID.randomUUID());
        List<Server> servers = Collections.emptyList();

        DeleteServerResponse result = createService(servers)
                .deleteServer(new DeleteServerRequest(serverID, KeepAttachedVolumes));

        assertThat(result).isEqualTo(new CannotDeleteNonExistingServer(serverID));
    }

    private DeleteServerApplication createService(List<Server> servers) {
        var serversRepository = new InMemoryServersRepositoryAdapter(servers);
        return new DeleteServerApplication(serversRepository);
    }


    private Server aServer(List<Volume> volumes) {
        return new Server(new ServerID(UUID.randomUUID()), volumes);
    }

    private Volume aVolume() {
        return new Volume(new VolumeID(UUID.randomUUID()));
    }
}
