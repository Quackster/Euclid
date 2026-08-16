package euclid.util;

import java.time.Instant;

public class DateUtil {

    public static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }

    public static long getUnixTimestampInMillis() {
        return Instant.now().toEpochMilli();
    }
}
