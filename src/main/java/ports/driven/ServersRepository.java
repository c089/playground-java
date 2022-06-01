package ports.driven;

import domain.ServerID;
import domain.Volume;

import java.util.stream.Stream;

public interface ServersRepository {
    boolean serverExists(ServerID serverID);

    Stream<Volume> findVolumesAttachedTo(ServerID server);
}
