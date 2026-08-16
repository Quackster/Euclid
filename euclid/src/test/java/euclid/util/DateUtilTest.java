package euclid.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateUtilTest {

    @Test
    void unixTimestampIsCurrent() {
        long before = System.currentTimeMillis() / 1000;
        long ts = DateUtil.getUnixTimestamp();
        long after = System.currentTimeMillis() / 1000;
        assertTrue(ts >= before && ts <= after, "timestamp " + ts + " outside [" + before + "," + after + "]");
    }

    @Test
    void unixTimestampInMillisIsCurrent() {
        long before = System.currentTimeMillis();
        long ts = DateUtil.getUnixTimestampInMillis();
        long after = System.currentTimeMillis();
        assertTrue(ts >= before && ts <= after, "timestamp " + ts + " outside [" + before + "," + after + "]");
    }
}
