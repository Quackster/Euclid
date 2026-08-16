package euclid.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    @Test
    void getEncodingIsIso88591() {
        assertEquals(StandardCharsets.ISO_8859_1, StringUtil.getEncoding());
    }

    @Test
    void hasWhitelistedCharactersNullReturnsFalse() {
        assertFalse(StringUtil.hasWhitelistedCharacters(null, "abc"));
    }

    @Test
    void hasWhitelistedCharactersEmptyReturnsTrue() {
        assertTrue(StringUtil.hasWhitelistedCharacters("", "abc"));
    }

    @Test
    void hasWhitelistedCharactersAllAllowed() {
        assertTrue(StringUtil.hasWhitelistedCharacters("abc", "abc"));
        assertTrue(StringUtil.hasWhitelistedCharacters("abc", "abcd"));
    }

    @Test
    void hasWhitelistedCharactersDisallowed() {
        assertFalse(StringUtil.hasWhitelistedCharacters("abcd", "abc"));
        assertFalse(StringUtil.hasWhitelistedCharacters("abX", "abc"));
    }
}
