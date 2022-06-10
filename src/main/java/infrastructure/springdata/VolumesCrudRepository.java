package infrastructure.springdata;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface VolumesCrudRepository extends CrudRepository<VolumeEntity, UUID> {
}
