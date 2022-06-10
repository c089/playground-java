package infrastructure.springdata;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface ServersCrudRepository extends CrudRepository<ServerEntity, UUID> {
}
