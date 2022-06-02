package domain;

import java.util.Objects;
import java.util.UUID;

public record VolumeID(UUID id) implements ResourceID {
    public VolumeID {
        Objects.requireNonNull(id);
    }
}
