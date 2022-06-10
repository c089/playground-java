package domain;

import java.util.Set;

public sealed interface DeleteServerResponse {
    record DeletionRequestAccepted(Set<? extends ResourceID> resourcesToDelete) implements DeleteServerResponse {
    }

    record CannotDeleteNonExistingServer(ServerID uuid) implements DeleteServerResponse {
    }
}
