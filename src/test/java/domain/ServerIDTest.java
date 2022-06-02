package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerIDTest {
    @Test
    void refusesNullId() {
        assertThrows(NullPointerException.class, () -> new ServerID(null));
    }
}