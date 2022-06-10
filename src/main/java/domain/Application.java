package domain;

import domain.DeleteServerResponse.CannotDeleteNonExistingServer;
import domain.DeleteServerResponse.DeletionRequestAccepted;
import ports.driven.ServersRepository;
import ports.driving.UseCase;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application implements UseCase {
    private final ServersRepository serversRepository;

    public Application(ServersRepository serversRepository) {
        this.serversRepository = serversRepository;
    }

    @Override
    public DeleteServerResponse deleteServer(DeleteServerRequest request) {
        if (serversRepository.findServerById(request.serverID()).isEmpty()) {
            return new CannotDeleteNonExistingServer(request.serverID());
        }

        var affectedResources = switch (request.deleteAttachedVolumes()) {
            case DeleteAttachedVolumes ->
                    serverAndVolumes(request.serverID(), serversRepository.findVolumesAttachedTo(request.serverID()));
            case KeepAttachedVolumes -> onlyTheServer(request.serverID());
        };
        return new DeletionRequestAccepted(affectedResources);
    }

    @Override
    public void attachVolumeToServer(ServerID server, VolumeID volume) {
        serversRepository.attachVolume(server, volume);
    }

    private Set<ServerID> onlyTheServer(ServerID serverID) {
        return Set.of(serverID);
    }

    private Set<? extends ResourceID> serverAndVolumes(ServerID serverID, Set<Volume> volumes) {
        return Stream.concat(Stream.of(serverID), volumes.stream().map(Volume::id)).collect(Collectors.toSet());
    }

    @Override
    public Server createServer() {
        return serversRepository.createServer();
    }

    @Override
    public Volume createVolume() {
        return serversRepository.createVolume();
    }
}
