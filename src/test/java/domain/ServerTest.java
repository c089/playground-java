package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerTest {

    @Test
    void refusesNullForId() {
        assertThrows(NullPointerException.class, () -> new Server(null));
    }

}