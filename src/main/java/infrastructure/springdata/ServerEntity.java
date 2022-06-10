package infrastructure.springdata;

import domain.Server;
import domain.ServerID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "server")
class ServerEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "server")
    private final Set<VolumeEntity> volumes = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    Server toDomain() {
        return new Server(new ServerID(getId()));
    }

    public Set<VolumeEntity> getVolumes() {
        return volumes;
    }

}
