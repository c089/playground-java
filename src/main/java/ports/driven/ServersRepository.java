package ports.driven;

import domain.Server;
import domain.ServerID;
import domain.Volume;
import domain.VolumeID;

import java.util.Optional;
import java.util.Set;

public interface ServersRepository {
    Server createServer();

    Set<Volume> findVolumesAttachedTo(ServerID server);

    Optional<Server> findServerById(ServerID id);

    Volume createVolume();

    void attachVolume(ServerID id, VolumeID volume);

    Optional<Volume> findVolumeById(VolumeID id);
}
