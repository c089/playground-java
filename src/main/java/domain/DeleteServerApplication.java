package domain;

import domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import domain.DeleteServerResponse.DeletionRequestAccepted;
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
    public DeleteServerResponse deleteServer(DeleteServerRequest request) {
        if (!serversRepository.serverExists(request.serverID())) {
            return new CannotDeleteNonExistingServer(request.serverID());
        }

        var affectedResources = switch (request.deleteAttachedVolumes()) {
            case DeleteAttachedVolumes ->
                    serverAndVolumes(request.serverID(), serversRepository.findVolumesAttachedTo(request.serverID()));
            case KeepAttachedVolumes -> onlyTheServer(request.serverID());
        };
        return new DeletionRequestAccepted(affectedResources);
    }

    private List<ServerID> onlyTheServer(ServerID serverID) {
        return List.of(serverID);
    }

    private List<? extends ResourceID> serverAndVolumes(ServerID serverID, Stream<Volume> volumes) {
        return Stream.concat(Stream.of(serverID), volumes.map(Volume::id)).toList();
    }

}
