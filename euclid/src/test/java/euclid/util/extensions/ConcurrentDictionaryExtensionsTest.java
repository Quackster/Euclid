package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrentDictionaryExtensionsTest {

    @Test
    void removeExistingReturnsTrue() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("a", 1);

        assertTrue(ConcurrentDictionaryExtensions.remove(map, "a"));
        assertFalse(map.containsKey("a"));
    }

    @Test
    void removeMissingReturnsFalse() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("a", 1);

        assertFalse(ConcurrentDictionaryExtensions.remove(map, "b"));
        assertTrue(map.containsKey("a"));
    }
}
