package euclid.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static Charset getEncoding() {
        return StandardCharsets.ISO_8859_1;
    }

    public static boolean hasWhitelistedCharacters(String str, String allowedChars) {
        if (str == null) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (allowedChars.indexOf(str.charAt(i)) >= 0) {
                continue;
            }

            return false;
        }

        return true;
    }
}
