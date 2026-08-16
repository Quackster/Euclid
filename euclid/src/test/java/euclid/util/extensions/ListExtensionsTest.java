package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListExtensionsTest {

    @Test
    void create() {
        assertEquals(List.of(1, 2, 3), ListExtensions.create(1, 2, 3));
        assertEquals(List.of("a", "b"), ListExtensions.create("a", "b"));
        assertTrue(ListExtensions.create().isEmpty());
    }

    @Test
    void createIsMutable() {
        List<Integer> list = ListExtensions.create(1, 2);
        list.add(3);
        assertEquals(List.of(1, 2, 3), list);
    }
}
