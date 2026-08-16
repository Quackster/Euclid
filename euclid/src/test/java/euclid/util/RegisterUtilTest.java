package euclid.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterUtilTest {

    @Test
    void nullIsInvalid() {
        assertFalse(RegisterUtil.isValidName(null));
    }

    @Test
    void tooShortIsInvalid() {
        assertFalse(RegisterUtil.isValidName("ab"));
        assertFalse(RegisterUtil.isValidName(""));
    }

    @Test
    void tooLongIsInvalid() {
        assertFalse(RegisterUtil.isValidName("abcdefghijklmnopqrstu"));
    }

    @Test
    void validNames() {
        assertTrue(RegisterUtil.isValidName("abc"));
        assertTrue(RegisterUtil.isValidName("abcdefghijklmnopqrst"));
        assertTrue(RegisterUtil.isValidName("John"));
        assertTrue(RegisterUtil.isValidName("UPPER"));
        assertTrue(RegisterUtil.isValidName("test123"));
        assertTrue(RegisterUtil.isValidName("a-b"));
        assertTrue(RegisterUtil.isValidName("a?b"));
    }

    @Test
    void reservedPrefixesAreInvalid() {
        assertFalse(RegisterUtil.isValidName("MOD-test"));
        assertFalse(RegisterUtil.isValidName("M0D-test"));
    }

    @Test
    void lowercaseModPrefixIsNotBlocked() {
        // C# only blocks exact-case "MOD-" and "M0D-"; "m0d-" passes the char check.
        assertTrue(RegisterUtil.isValidName("m0d-test"));
    }

    @Test
    void disallowedCharactersAreInvalid() {
        assertFalse(RegisterUtil.isValidName("user#"));
        assertFalse(RegisterUtil.isValidName("a b"));
        assertFalse(RegisterUtil.isValidName("a_b"));
    }
}
