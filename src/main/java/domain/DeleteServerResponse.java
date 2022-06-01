package domain;

import java.util.List;

public sealed interface DeleteServerResponse {
    record DeletionRequestAccepted(List<? extends ResourceID> resourcesToDelete) implements DeleteServerResponse {
    }

    record CannotDeleteNonExistingServer(ServerID uuid) implements DeleteServerResponse {
    }
}
