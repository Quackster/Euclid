package euclid.util.extensions;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentDictionaryExtensions {

    /**
     * Remove from a map, return if successful
     */
    public static <K, V> boolean remove(ConcurrentHashMap<K, V> dictionary, K key) {
        V value = dictionary.remove(key);
        return value != null;
    }
}
