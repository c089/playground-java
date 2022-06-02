package domain;

import java.util.Objects;
import java.util.UUID;

public record ServerID(UUID id) implements ResourceID {
    public ServerID {
        Objects.requireNonNull(id);
    }
}
