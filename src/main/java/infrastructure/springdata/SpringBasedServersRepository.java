package infrastructure.springdata;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface SpringBasedServersRepository extends CrudRepository<ServerEntity, UUID> {
}
