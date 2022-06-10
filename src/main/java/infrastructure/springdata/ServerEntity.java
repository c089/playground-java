package infrastructure.springdata;

import domain.Server;
import domain.ServerID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collections;
import java.util.UUID;

@Entity
class ServerEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    Server toDomain() {
        return new Server(new ServerID(getId()), Collections.emptyList());
    }
}
