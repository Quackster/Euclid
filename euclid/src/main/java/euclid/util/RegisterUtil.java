package euclid.util;

public class RegisterUtil {

    public static boolean isValidName(String name) {
        // FAILproof!
        if (name != null) {

            // Atleast 3 characters and not more than 20?
            if (name.length() >= 3 && name.length() <= 20) {
                // Does username start with MOD- ?
                if (!name.startsWith("MOD-")) {

                    // We don't want m0d neither...
                    if (!name.startsWith("M0D-")) {
                        // Check for characters
                        String allowed = "1234567890qwertyuiopasdfghjklzxcvbnm-=?!@:.,";

                        if (allowed.equals("*")) {
                            // Any name can pass!
                            return true;
                        } else {

                            // Check each character in the name
                            char[] nameChars = name.toCharArray();

                            for (int i = 0; i < nameChars.length; i++) {

                                // Is this character allowed?
                                if (allowed.indexOf(Character.toLowerCase(nameChars[i])) < 0) {
                                    // Not allowed
                                    return false;
                                }
                            }

                            // Passed all checks!
                            return true;
                        }
                    }
                }
            }
        }

        // Bad for whatever reason!
        return false;
    }
}
