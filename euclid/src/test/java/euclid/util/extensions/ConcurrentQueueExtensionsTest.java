package euclid.util.extensions;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrentQueueExtensionsTest {

    @Test
    void dequeueEmpty() {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        assertTrue(ConcurrentQueueExtensions.dequeue(queue).isEmpty());
    }

    @Test
    void dequeueDrainsInOrder() {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(List.of(1, 2, 3, 4, 5));
        List<Integer> drained = ConcurrentQueueExtensions.dequeue(queue);

        assertEquals(List.of(1, 2, 3, 4, 5), drained);
        assertTrue(queue.isEmpty());
    }
}
