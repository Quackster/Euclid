package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongExtensionsTest {

    @Test
    void toDateTime() {
        assertEquals(Instant.EPOCH, LongExtensions.toDateTime(0));
        assertEquals(Instant.ofEpochSecond(1000000000L), LongExtensions.toDateTime(1000000000L));
        assertEquals(Instant.ofEpochSecond(-1000L), LongExtensions.toDateTime(-1000L));
    }
}
