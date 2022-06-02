package domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerTest {

    @Test
    void refusesNullForId() {
        assertThrows(NullPointerException.class, () -> new Server(null, Collections.emptyList()));
    }

    @Test
    void refusesNullForListOfVolumes() {
        assertThrows(NullPointerException.class, () -> new Server(new ServerID(UUID.randomUUID()), null));
    }
}