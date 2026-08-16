package euclid.util.specialised;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64EncodingTest {

    private static byte[] hex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }

    @Test
    void encodesKnownVectors() {
        assertArrayEquals(hex("41"), Base64Encoding.encodeInt32(1, 1));
        assertArrayEquals(hex("4041"), Base64Encoding.encodeInt32(1, 2));
        assertArrayEquals(hex("7F"), Base64Encoding.encodeInt32(63, 1));
        assertArrayEquals(hex("4140"), Base64Encoding.encodeInt32(64, 2));
        assertArrayEquals(hex("437F"), Base64Encoding.encodeInt32(255, 2));
        assertArrayEquals(hex("4440"), Base64Encoding.encodeInt32(256, 2));
        assertArrayEquals(hex("7F7F"), Base64Encoding.encodeInt32(4095, 2));
        assertArrayEquals(hex("407F7F"), Base64Encoding.encodeInt32(4095, 3));
        assertArrayEquals(hex("414040"), Base64Encoding.encodeInt32(4096, 3));
        assertArrayEquals(hex("404F7F7F"), Base64Encoding.encodeInt32(65535, 4));
        assertArrayEquals(hex("504040"), Base64Encoding.encodeInt32(65536, 3));
        assertArrayEquals(hex("585A60"), Base64Encoding.encodeInt32(100000, 3));
        assertArrayEquals(hex("7F7F7F7F7F"), Base64Encoding.encodeInt32(2147483647, 5));
    }

    @Test
    void decodesKnownVectors() {
        assertEquals(1, Base64Encoding.decodeInt32(hex("41")));
        assertEquals(1, Base64Encoding.decodeInt32(hex("4041")));
        assertEquals(64, Base64Encoding.decodeInt32(hex("4140")));
        assertEquals(255, Base64Encoding.decodeInt32(hex("437F")));
        assertEquals(4095, Base64Encoding.decodeInt32(hex("7F7F")));
        assertEquals(4096, Base64Encoding.decodeInt32(hex("414040")));
        assertEquals(65535, Base64Encoding.decodeInt32(hex("4F7F7F")));
        assertEquals(100000, Base64Encoding.decodeInt32(hex("585A60")));
        assertEquals(1073741823, Base64Encoding.decodeInt32(hex("7F7F7F7F7F")));
    }

    @Test
    void truncatesOutOfRangeValues() {
        assertEquals(0, Base64Encoding.decodeInt32(Base64Encoding.encodeInt32(64, 1)));
        assertEquals(63, Base64Encoding.decodeInt32(Base64Encoding.encodeInt32(255, 1)));
        assertEquals(59, Base64Encoding.decodeInt32(Base64Encoding.encodeInt32(-5, 1)));
    }

    @Test
    void roundTripsFittingValues() {
        for (int value : new int[] {0, 1, 63, 255, 4095, 65535, 100000}) {
            byte[] encoded = Base64Encoding.encodeInt32(value, 5);
            assertEquals(value, Base64Encoding.decodeInt32(encoded), "round-trip " + value);
        }
    }
}
