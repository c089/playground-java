package infrastructure;

import domain.Server;
import domain.ServerID;
import domain.Volume;
import domain.VolumeID;
import ports.driven.ServersRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryServersRepositoryAdapter implements ServersRepository {
    private final Set<Volume> volumes;
    private final Set<Attachment> volumeAttachments;
    private List<Server> servers;

    public InMemoryServersRepositoryAdapter() {
        this.servers = new ArrayList<>();
        this.volumes = new HashSet<>();
        volumeAttachments = new HashSet<>();
    }

    public Set<Volume> findVolumesAttachedTo(ServerID server) {
        final Set<Attachment> attachments = volumeAttachments.stream().filter(x -> x.server.equals(server)).collect(Collectors.toSet());
        final Set<VolumeID> volumeIds = attachments.stream().map(x -> x.volume).collect(Collectors.toSet());
        return volumes.stream().filter(o -> volumeIds.contains(o.id())).collect(Collectors.toSet());
    }

    @Override
    public Server createServer() {
        final Server newServer = new Server(new ServerID(UUID.randomUUID()));
        this.servers = Stream.concat(this.servers.stream(), Stream.of(newServer)).toList();
        return newServer;
    }

    @Override
    public Optional<Server> findServerById(ServerID id) {
        return servers.stream().filter(x -> x.id().equals(id)).findFirst();
    }

    @Override
    public Volume createVolume() {
        final Volume volume = new Volume(new VolumeID(UUID.randomUUID()));
        this.volumes.add(volume);
        return volume;
    }

    @Override
    public void attachVolume(ServerID id, VolumeID volumeId) {
        volumeAttachments.add(new Attachment(id, volumeId));
    }

    @Override
    public Optional<Volume> findVolumeById(VolumeID id) {
        return volumes.stream().filter(x -> x.id().equals(id)).findFirst();
    }

    record Attachment(ServerID server, VolumeID volume) {
    }
}