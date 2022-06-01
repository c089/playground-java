package domain;

import ports.driven.ServersRepository;
import ports.driving.DeleteServerUseCase;

import java.util.List;
import java.util.stream.Stream;

public class DeleteServerApplication implements DeleteServerUseCase {
    private final ServersRepository serversRepository;

    public DeleteServerApplication(ServersRepository serversRepository) {
        this.serversRepository = serversRepository;
    }

    @Override
    public DeleteServerResponse deleteServer(DeleteServerRequest deleteServerRequest) {
        if (!serversRepository.serverExists(deleteServerRequest.serverID())) {
            return new DeleteServerResponse.CannotDeleteNonExistingServer(deleteServerRequest.serverID());
        }

        var affectedResources = switch (deleteServerRequest.deleteAttachedVolumes()) {
            case DeleteAttachedVolumes ->
                    Stream.concat(Stream.of(deleteServerRequest.serverID()), serversRepository.findVolumesAttachedTo(deleteServerRequest.serverID()).map(Volume::id)).toList();
            case KeepAttachedVolumes -> List.of(deleteServerRequest.serverID());
        };
        return new DeleteServerResponse.DeletionRequestAccepted(affectedResources);
    }

}
