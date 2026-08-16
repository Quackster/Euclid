package euclid.util.encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretKeyTest {

    @Test
    void decodesKnownVectors() {
        assertEquals(13, SecretKey.secretDecode("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(0, SecretKey.secretDecode("abcdefghijklmnopqrstuvwxyz0123456789"));
        assertEquals(302, SecretKey.secretDecode("0123456789abcdef0123456789abcdef"));
        assertEquals(302, SecretKey.secretDecode("ABCDEFGHIJKLMNOPABCDEFGHIJKLMNOP"));
    }
}
