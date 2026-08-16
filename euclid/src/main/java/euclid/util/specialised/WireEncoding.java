package euclid.util.specialised;

public class WireEncoding {

    public static final byte NEGATIVE = 72; // 'H'
    public static final byte POSITIVE = 73; // 'I'
    public static final int MAX_INTEGER_BYTE_AMOUNT = 6;

    public record Decoded(int value, int totalBytes) {
    }

    public static byte[] encodeInt32(int i) {
        byte[] wf = new byte[MAX_INTEGER_BYTE_AMOUNT];

        int pos = 0;
        int numBytes = 1;
        int startPos = pos;
        int negativeMask = i >= 0 ? 0 : 4;

        i = Math.abs(i);
        wf[pos++] = (byte) (64 + (i & 3));

        for (i >>= 2; i != 0; i >>= MAX_INTEGER_BYTE_AMOUNT) {
            numBytes++;
            wf[pos++] = (byte) (64 + (i & 0x3f));
        }

        wf[startPos] = (byte) (wf[startPos] | numBytes << 3 | negativeMask);
        byte[] bzData = new byte[numBytes];

        for (int x = 0; x < numBytes; x++) {
            bzData[x] = wf[x];
        }

        return bzData;
    }

    public static Decoded decodeInt32(byte[] bzData) {
        int pos = 0;

        boolean negative = (bzData[pos] & 4) == 4;
        int totalBytes = bzData[pos] >> 3 & 7;

        int v = bzData[pos] & 3;
        pos++;

        int shiftAmount = 2;

        for (int b = 1; b < totalBytes; b++) {
            v |= (bzData[pos] & 0x3f) << shiftAmount;
            shiftAmount = 2 + 6 * b;
            pos++;
        }

        if (negative)
            v *= -1;

        return new Decoded(v, totalBytes);
    }
}
