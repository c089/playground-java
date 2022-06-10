package infrastructure.springdata;

import domain.Server;
import domain.ServerID;
import domain.Volume;
import ports.driven.ServersRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

class SpringDataServerRepositoryAdapter implements ServersRepository {
    private final SpringBasedServersRepository repository;

    public SpringDataServerRepositoryAdapter(SpringBasedServersRepository repository) {
        this.repository = repository;
    }

    @Override
    public Server createServer() {
        final ServerEntity entity = new ServerEntity();
        entity.setId(UUID.randomUUID());
        repository.save(entity);
        return entity.toDomain();
    }

    @Override
    public Optional<Server> findServerById(ServerID id) {
        return this.repository.findById(id.id()).map(ServerEntity::toDomain);
    }

    @Override
    public Stream<Volume> findVolumesAttachedTo(ServerID server) {
        return Stream.empty();
    }
}
