package ports.driven;

import domain.Server;
import domain.ServerID;
import domain.Volume;

import java.util.stream.Stream;

public interface ServersRepository {
    Server createServer();

    boolean serverExists(ServerID serverID);

    Stream<Volume> findVolumesAttachedTo(ServerID server);
}
