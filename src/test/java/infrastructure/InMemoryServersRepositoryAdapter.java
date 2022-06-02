package infrastructure;

import domain.Server;
import domain.ServerID;
import domain.Volume;
import ports.driven.ServersRepository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class InMemoryServersRepositoryAdapter implements ServersRepository {
    List<Server> servers;

    public InMemoryServersRepositoryAdapter(List<Server> servers) {
        this.servers = servers;
    }

    public boolean serverExists(ServerID serverID) {
        return this.servers.stream().map(Server::id).anyMatch(x -> x.equals(serverID));
    }

    public Stream<Volume> findVolumesAttachedTo(ServerID server) {
        return servers
                .stream()
                .filter(x -> x.id().equals(server)).findFirst().map(Server::volumes)
                .orElse(Collections.emptyList())
                .stream();
    }

    @Override
    public Server createServer() {
        final Server newServer = new Server(new ServerID(UUID.randomUUID()), Collections.emptyList());
        this.servers = Stream.concat(this.servers.stream(), Stream.of(newServer)).toList();
        return newServer;
    }
}