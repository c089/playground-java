package domain;

import java.util.Objects;

public record Server(ServerID id) implements Resource {
    public Server {
        Objects.requireNonNull(id);
    }
}
