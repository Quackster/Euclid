package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnumerableExtensionTest {

    private static List<Integer> range(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    @Test
    void getPage() {
        List<Integer> list = range(10);
        assertEquals(List.of(0, 1, 2), EnumerableExtension.getPage(list, 0, 3));
        assertEquals(List.of(3, 4, 5), EnumerableExtension.getPage(list, 1, 3));
        assertEquals(List.of(9), EnumerableExtension.getPage(list, 3, 3));
        assertEquals(List.of(), EnumerableExtension.getPage(list, 4, 3));
    }

    @Test
    void countPages() {
        assertEquals(4, EnumerableExtension.countPages(range(10), 3));
        assertEquals(3, EnumerableExtension.countPages(range(9), 3));
        assertEquals(1, EnumerableExtension.countPages(range(0), 3));
        assertEquals(1, EnumerableExtension.countPages(range(1), 5));
    }

    @Test
    void shuffleKeepsAllElements() {
        List<Integer> list = range(20);
        List<Integer> shuffled = EnumerableExtension.shuffle(list);
        assertEquals(list.size(), shuffled.size());
        assertEquals(new HashSet<>(list), new HashSet<>(shuffled));
        // Original is not mutated.
        assertEquals(range(20), list);
    }

    @Test
    void pickRandomReturnsElement() {
        List<Integer> list = range(10);
        Integer picked = EnumerableExtension.pickRandom(list);
        assertTrue(list.contains(picked));
    }

    @Test
    void pickRandomCount() {
        List<Integer> list = range(10);
        assertEquals(3, EnumerableExtension.pickRandom(list, 3).size());
        assertEquals(10, EnumerableExtension.pickRandom(list, 100).size());
    }
}
