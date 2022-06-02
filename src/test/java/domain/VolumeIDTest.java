package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class VolumeIDTest {
    @Test
    void refusesNullForId() {
        assertThrows(NullPointerException.class, () -> new VolumeID(null));
    }
}