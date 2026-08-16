package euclid.util.encryption;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Habbo Hotel V1 RC4 class by Quackster (Alex)
 * Written in December 2021
 */
public class RC4 {

    private int i;
    private int k;
    private int j;

    private int[] sbox;
    private int[] skey;

    /**
     * Create key, not used, but ported from Habbo V1 RC4 class written in Lingo
     */
    public int createKey() {
        StringBuilder sb = new StringBuilder();
        var random = ThreadLocalRandom.current();
        int a = 0;

        while (a < 4) {
            sb.append(int2hex(random.nextInt(256) - 1));
            a++;
        }

        return Math.abs(hex2int(sb.toString()));
    }

    /**
     * Initialise the encryption class
     */
    public void setKey(int myKey) {
        skey = new int[256];
        sbox = new int[256];

        i = 0;
        k = 0;

        String tempKey = Integer.toString(myKey);

        while (i <= 255) {
            skey[i] = tempKey.charAt(i % tempKey.length());
            sbox[i] = i;
            i = 1 + i;
        }

        i = 0;
        j = 0;

        while (i <= 255) {
            j = (j + sbox[i] + skey[i]) % 256;
            k = sbox[i];
            sbox[i] = sbox[j];
            sbox[j] = k;
            i = 1 + i;
        }

        i = 0;
        j = 0;
    }

    /**
     * Recreate the enciphering performed on the client
     */
    public String encipher(String data) {
        StringBuilder cipher = new StringBuilder();
        int a = 0;

        while (a < data.length()) {
            cipher.append(int2hex(data.charAt(a) ^ shift()));
            a++;
        }

        return cipher.toString();
    }

    /**
     * Decipher incoming packets from client
     */
    public String decipher(String data) {
        StringBuilder cipher = new StringBuilder();
        int a = 0;

        while (a < data.length()) {
            int t = hex2int(data.substring(a, a + 2));
            cipher.append((char) (t ^ shift()));
            a += 2;
        }

        return cipher.toString();
    }

    /**
     * Shift RC4 tables
     */
    private int shift() {
        i = (i + 1) % 256;
        j = (j + sbox[i]) % 256;
        int temp = sbox[i];
        sbox[i] = sbox[j];
        sbox[j] = temp;
        return sbox[(sbox[i] + sbox[j]) % 256];
    }

    /**
     * int2hex ported from Lingo
     */
    public static String int2hex(int aint) {
        String digits = "0123456789ABCDEF";
        StringBuilder hexstr = new StringBuilder();

        if (aint <= 0) {
            hexstr.append("00");
        } else {
            while (aint > 0) {
                int sd = (aint % 16);
                aint /= 16;
                hexstr.insert(0, digits.charAt(sd));
            }
        }

        if ((hexstr.length() % 2) == 1) {
            hexstr.insert(0, "0");
        }

        return hexstr.toString();
    }

    /**
     * hex2int method used for deciphering
     */
    public static int hex2int(String ahex) {
        return Integer.parseInt(ahex, 16);
    }
}
