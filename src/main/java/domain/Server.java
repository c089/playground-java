package domain;

import java.util.List;

public record Server(ServerID id, List<Volume> volumes) implements Resource {
}
