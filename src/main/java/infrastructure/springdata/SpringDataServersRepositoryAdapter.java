package infrastructure.springdata;

import domain.Server;
import domain.ServerID;
import domain.Volume;
import domain.VolumeID;
import ports.driven.ServersRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class SpringDataServersRepositoryAdapter implements ServersRepository {
    private final ServersCrudRepository serversCrudRepository;
    private final VolumesCrudRepository volumesCrudRepository;

    public SpringDataServersRepositoryAdapter(ServersCrudRepository serversCrudRepository, VolumesCrudRepository volumesCrudRepository) {
        this.serversCrudRepository = serversCrudRepository;
        this.volumesCrudRepository = volumesCrudRepository;
    }

    @Override
    public Server createServer() {
        final ServerEntity entity = new ServerEntity();
        entity.setId(UUID.randomUUID());
        return serversCrudRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Server> findServerById(ServerID id) {
        return findServerEntity(this.serversCrudRepository, id).map(ServerEntity::toDomain);
    }

    @Override
    public Set<Volume> findVolumesAttachedTo(ServerID server) {
        return findServerEntity(this.serversCrudRepository, server)
                .map(s -> s.getVolumes().stream().map(VolumeEntity::toDomain).collect(Collectors.toSet()))
                .orElseThrow();
    }

    @Override
    public Volume createVolume() {
        final VolumeEntity entity = new VolumeEntity();
        entity.setId(UUID.randomUUID());
        return volumesCrudRepository.save(entity).toDomain();
    }

    @Override
    public void attachVolume(ServerID id, VolumeID volumeID) {
        final Optional<ServerEntity> maybeServer = findServerEntity(serversCrudRepository, id);
        final Optional<VolumeEntity> maybeVolume = volumesCrudRepository.findById(volumeID.id());
        maybeServer.flatMap(s ->
                maybeVolume.map(v -> {
                            v.setServer(s);
                            s.getVolumes().add(v);
                            serversCrudRepository.save(s);
                            return volumesCrudRepository.save(v);
                        }
                )).orElseThrow();
    }

    @Override
    public Optional<Volume> findVolumeById(VolumeID id) {
        return volumesCrudRepository.findById(id.id()).map(VolumeEntity::toDomain);
    }

    private Optional<ServerEntity> findServerEntity(ServersCrudRepository serversRepository, ServerID server) {
        return serversRepository.findById(server.id());
    }
}
