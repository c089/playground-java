package domain;

import java.util.Objects;

public record Volume(VolumeID id) implements Resource {
    public Volume {
        Objects.requireNonNull(id);
    }
}
