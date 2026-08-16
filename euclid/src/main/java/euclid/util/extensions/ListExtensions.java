package euclid.util.extensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListExtensions {

    public static <T> List<T> create(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }
}
