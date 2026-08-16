package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringExtensionsTest {

    @Test
    void filterInputReplacesControlCharsAndHash() {
        assertEquals("a b c*", StringExtensions.filterInput("a\1b\nc#"));
        assertEquals("x", StringExtensions.filterInput("x"));
        assertEquals("  ", StringExtensions.filterInput("\t\r"));
    }

    @Test
    void isNumeric() {
        assertTrue(StringExtensions.isNumeric("123"));
        assertTrue(StringExtensions.isNumeric("-5"));
        assertFalse(StringExtensions.isNumeric("12a"));
        assertFalse(StringExtensions.isNumeric(""));
        assertFalse(StringExtensions.isNumeric("1.5"));
    }

    @Test
    void toIntArraySplitsOnLiteralSeparator() {
        assertArrayEquals(new int[] {1, 2, 3}, StringExtensions.toIntArray("1,2,3", ','));
        assertArrayEquals(new int[] {1, 2, 3}, StringExtensions.toIntArray("1;2;3", ';'));
        // '.' is a regex metachar; must still split literally (C# Split(char) is literal).
        assertArrayEquals(new int[] {1, 2, 3}, StringExtensions.toIntArray("1.2.3", '.'));
    }

    @Test
    void toConsoleOutputEscapesControlChars() {
        assertEquals("a[1]b", StringExtensions.toConsoleOutput("a\1b"));
        assertEquals("[10]", StringExtensions.toConsoleOutput("\n"));
        assertEquals("plain", StringExtensions.toConsoleOutput("plain"));
    }
}
