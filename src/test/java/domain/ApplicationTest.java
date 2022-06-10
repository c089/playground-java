package domain;

import infrastructure.InMemoryServersRepositoryAdapter;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.DeleteAttachedVolumes;
import static domain.DeleteServerRequest.DeleteAttachedVolumesOption.KeepAttachedVolumes;
import static domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import static domain.DeleteServerResponse.DeletionRequestAccepted;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {
    @Test
    public void givenAnExistingServerWhenIDeleteTheServerWithoutVolumesThenOnlyTheServerIsDeleted() {
        final Application application = createApplication();
        final Server server = application.createServer();

        DeleteServerResponse result = application
                .deleteServer(new DeleteServerRequest(server.id(), KeepAttachedVolumes));

        assertThat(result).isEqualTo(new DeletionRequestAccepted(Set.of(server.id())));
    }

    @Test
    void givenAnExistingServerWhenIDeleteItIncludingVolumesTheyAreDeleted() {
        final Application application = createApplication();
        Server server = application.createServer();
        Volume volume1 = application.createVolume();
        Volume volume2 = application.createVolume();

        application.attachVolumeToServer(server.id(), volume1.id());
        application.attachVolumeToServer(server.id(), volume2.id());

        DeleteServerResponse result = application
                .deleteServer(new DeleteServerRequest(server.id(), DeleteAttachedVolumes));

        assertThat(result).isInstanceOf(DeletionRequestAccepted.class);
        assertThat(((DeletionRequestAccepted) result).resourcesToDelete()).isEqualTo(Set.of(server.id(), volume1.id(), volume2.id()));
    }

    @Test
    void givenARequestToDeleteANonExistingServerIAmToldTheServerDoesNotExist() {
        final ServerID serverID = new ServerID(UUID.randomUUID());

        DeleteServerResponse result = createApplication()
                .deleteServer(new DeleteServerRequest(serverID, KeepAttachedVolumes));

        assertThat(result).isEqualTo(new CannotDeleteNonExistingServer(serverID));
    }

    private Application createApplication() {
        var serversRepository = new InMemoryServersRepositoryAdapter();
        return new Application(serversRepository);
    }


}
