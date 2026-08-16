package euclid.util.specialised;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WireEncodingTest {

    private static byte[] hex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }

    private static void assertVector(int value, String expectedHex) {
        byte[] encoded = WireEncoding.encodeInt32(value);
        assertArrayEquals(hex(expectedHex), encoded, "encode(" + value + ")");
        WireEncoding.Decoded decoded = WireEncoding.decodeInt32(encoded);
        assertEquals(value, decoded.value(), "decode value for " + value);
        assertEquals(encoded.length, decoded.totalBytes(), "totalBytes for " + value);
    }

    @Test
    void encodesKnownVectors() {
        assertVector(0, "48");
        assertVector(1, "49");
        assertVector(-1, "4D");
        assertVector(2, "4A");
        assertVector(3, "4B");
        assertVector(63, "534F");
        assertVector(64, "5050");
        assertVector(65, "5150");
        assertVector(255, "537F");
        assertVector(256, "584041");
        assertVector(1000, "587A43");
        assertVector(4095, "5B7F4F");
        assertVector(4096, "584050");
        assertVector(-4096, "5C4050");
        assertVector(65535, "637F7F43");
        assertVector(65536, "60404044");
        assertVector(-65536, "64404044");
        assertVector(100000, "60684646");
        assertVector(-100000, "64684646");
        assertVector(123456, "60506247");
        assertVector(-123456, "64506247");
        assertVector(2147483647, "737F7F7F7F5F");
        assertVector(16777215, "6B7F7F7F4F");
        assertVector(-16777216, "6C40404050");
    }

    @Test
    void roundTripsRandomValues() {
        java.util.Random random = new java.util.Random(42);
        for (int n = 0; n < 1000; n++) {
            int value = random.nextInt();
            if (value == Integer.MIN_VALUE) {
                continue;
            }
            byte[] encoded = WireEncoding.encodeInt32(value);
            WireEncoding.Decoded decoded = WireEncoding.decodeInt32(encoded);
            assertEquals(value, decoded.value(), "round-trip " + value);
        }
    }
}
