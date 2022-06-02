package domain;

import java.util.List;
import java.util.Objects;

public record Server(ServerID id, List<Volume> volumes) implements Resource {
    public Server {
        Objects.requireNonNull(id);
        Objects.requireNonNull(volumes);
    }
}
