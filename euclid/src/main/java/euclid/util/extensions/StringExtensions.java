package euclid.util.extensions;

public class StringExtensions {

    /**
     * Filter harmful injectable characters
     */
    public static String filterInput(String str) {
        str = str.replace((char) 1, ' ');
        str = str.replace((char) 2, ' ');
        str = str.replace((char) 3, ' ');
        str = str.replace((char) 9, ' ');
        str = str.replace((char) 10, ' ');
        str = str.replace((char) 13, ' ');
        str = str.replace('#', '*');
        return str;
    }

    /**
     * Get if string is numeric
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Convert delimetered string to int array
     */
    public static int[] toIntArray(String value, char separator) {
        String[] parts = value.split(java.util.regex.Pattern.quote(String.valueOf(separator)), -1);
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    /**
     * Convert string to console output
     */
    public static String toConsoleOutput(String value) {
        String consoleText = value;

        for (int i = 0; i < 13; i++) {
            consoleText = consoleText.replace(String.valueOf((char) i), "[" + i + "]");
        }

        return consoleText;
    }
}
