package infrastructure.springdata;

import domain.Volume;
import domain.VolumeID;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "volume")
public class VolumeEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    Volume toDomain() {
        return new Volume(new VolumeID(id));
    }
}
