package infrastructure.springdata;

import domain.ServerID;
import org.springframework.data.repository.CrudRepository;

interface SpringBasedServersRepository extends CrudRepository<ServerEntity, ServerID> {
}
