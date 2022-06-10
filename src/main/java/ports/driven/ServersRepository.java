package ports.driven;

import domain.Server;
import domain.ServerID;
import domain.Volume;

import java.util.Optional;
import java.util.stream.Stream;

public interface ServersRepository {
    Server createServer();

    Stream<Volume> findVolumesAttachedTo(ServerID server);

    Optional<Server> findServerById(ServerID id);
}
