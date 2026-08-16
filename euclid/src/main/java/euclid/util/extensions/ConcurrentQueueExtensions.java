package euclid.util.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentQueueExtensions {

    /**
     * Drain to a list
     */
    public static <T> List<T> dequeue(ConcurrentLinkedQueue<T> queue) {
        List<T> list = new ArrayList<>();

        while (queue.size() > 0) {
            T element = queue.poll();
            if (element != null) {
                list.add(element);
            }
        }

        return list;
    }
}
