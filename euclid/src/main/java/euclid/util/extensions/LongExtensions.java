package euclid.util.extensions;

import java.time.Instant;

public class LongExtensions {

    /**
     * Converts unix timestamp to instant
     */
    public static Instant toDateTime(long value) {
        return Instant.ofEpochSecond(value);
    }
}
