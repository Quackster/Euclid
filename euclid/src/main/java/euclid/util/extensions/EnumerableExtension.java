package euclid.util.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumerableExtension {

    public static <T> T pickRandom(List<T> source) {
        return source.get(java.util.concurrent.ThreadLocalRandom.current().nextInt(source.size()));
    }

    public static <T> List<T> pickRandom(List<T> source, int count) {
        return shuffle(source).subList(0, Math.min(count, source.size()));
    }

    public static <T> List<T> shuffle(List<T> source) {
        List<T> copy = new ArrayList<>(source);
        Collections.shuffle(copy);
        return copy;
    }

    public static <T> List<T> getPage(List<T> list, int page, int pageSize) {
        int from = Math.min(page * pageSize, list.size());
        int to = Math.min(from + pageSize, list.size());
        return new ArrayList<>(list.subList(from, to));
    }

    public static <T> int countPages(List<T> list, int pageSize) {
        return ((list.size() - 1) / pageSize) + 1;
    }
}
