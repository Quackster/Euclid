package euclid.util.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RC4Test {

    @Test
    void int2hexKnownVectors() {
        assertEquals("00", RC4.int2hex(0));
        assertEquals("01", RC4.int2hex(1));
        assertEquals("00", RC4.int2hex(-1));
        assertEquals("0F", RC4.int2hex(15));
        assertEquals("10", RC4.int2hex(16));
        assertEquals("FF", RC4.int2hex(255));
        assertEquals("0100", RC4.int2hex(256));
        assertEquals("0FFF", RC4.int2hex(4095));
        assertEquals("0101", RC4.int2hex(257));
        assertEquals("FFFFFF", RC4.int2hex(16777215));
    }

    @Test
    void hex2intRoundTripsInt2hex() {
        for (int value : new int[] {0, 1, 15, 16, 255, 256, 4095, 257, 16777215}) {
            assertEquals(value, RC4.hex2int(RC4.int2hex(value)), "round-trip " + value);
        }
    }

    @Test
    void int2hexIsLossyForNonPositive() {
        // Matches C# behaviour: every value <= 0 encodes to "00", so it cannot round-trip.
        assertEquals(0, RC4.hex2int(RC4.int2hex(-1)));
        assertEquals(0, RC4.hex2int(RC4.int2hex(-256)));
    }

    @Test
    void encipherMatchesCSharpVectors() {
        assertEquals("11", encipher(12345, "A"));
        assertEquals("1177", encipher(12345, "AB"));
        assertEquals("1C7A7F2B623E780AE857F4C0B915A1", encipher(12345, "LOGIN test pass"));
        assertEquals("1850540E43322C38F451B8D4F9", encipher(12345, "Hello, World!"));
        assertEquals("31575B531E2D", encipher(12345, "abc123"));
        assertEquals("2C558524D9679E1707483DCD2B14D3", encipher(1, "LOGIN test pass"));
        assertEquals("F0DB3CD7D7EB6792A6D424F5E02928", encipher(999999, "LOGIN test pass"));
        assertEquals("18ED4A181343AAA2DC084FC53FE33E", encipher(16777215, "LOGIN test pass"));
        assertEquals("60ABC5284395F391F5461F5D32FCD3", encipher(42, "LOGIN test pass"));
    }

    @Test
    void decipherReversesEncipher() {
        int[] keys = {12345, 1, 999999, 16777215, 42};
        String[] messages = {"A", "AB", "LOGIN test pass", "Hello, World!", "abc123"};
        for (int key : keys) {
            for (String message : messages) {
                RC4 encrypt = new RC4();
                encrypt.setKey(key);
                String cipher = encrypt.encipher(message);

                RC4 decrypt = new RC4();
                decrypt.setKey(key);
                assertEquals(message, decrypt.decipher(cipher), "key=" + key + " msg=" + message);
            }
        }
    }

    @Test
    void decipherKnownVector() {
        RC4 decrypt = new RC4();
        decrypt.setKey(12345);
        assertEquals("LOGIN", decrypt.decipher("1C7A7F2B62"));
    }

    private static String encipher(int key, String message) {
        RC4 rc4 = new RC4();
        rc4.setKey(key);
        return rc4.encipher(message);
    }
}
