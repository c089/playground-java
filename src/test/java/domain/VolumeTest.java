package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class VolumeTest {
    @Test
    void refusesNullVolumeId() {
        assertThrows(NullPointerException.class, () -> new Volume(null));
    }
}