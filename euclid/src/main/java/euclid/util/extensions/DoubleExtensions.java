package euclid.util.extensions;

import java.util.Locale;

public class DoubleExtensions {

    /**
     * Convert double for Habbo client
     */
    public static String toClientValue(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }
}
