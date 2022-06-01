package domain;

import java.util.UUID;

public record ServerID(UUID id) implements ResourceID {
}
