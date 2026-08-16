package euclid.util.encryption;

/**
 * Habbo Hotel V1 RC4 class by Quackster (Alex)
 * Written in December 2021
 */
public class SecretKey {

    /**
     * Decode the secret sent from client
     */
    public static int secretDecode(String key) {
        String table = key.substring(0, key.length() / 2);
        String tempKey = key.substring(key.length() / 2);

        int checkSum = 0;
        int i = 0;

        while (i < tempKey.length()) {
            int a = table.indexOf(tempKey.charAt(i));

            if (a % 2 == 0) {
                a *= 2;
            }

            if (i % 3 == 0) {
                a *= 3;
            }

            if (a < 0) {
                a = (tempKey.length() % 2);
            }

            checkSum += a;
            i++;
        }

        return checkSum;
    }
}
