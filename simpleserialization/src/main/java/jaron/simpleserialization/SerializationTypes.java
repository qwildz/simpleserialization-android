package jaron.simpleserialization;

/**
 * The static <code>SerializationData</code> class provides some data type
 * functionality.
 *
 * @author jarontec gmail com
 * @version 1.0
 * @since 1.0
 */
public class SerializationTypes {
    public static final int SIZEOF_INTEGER = 4;
    public static final int SIZEOF_FLOAT = 4;
    public static final int SIZEOF_STRING = 4;
    public static final int SIZEOF_BOOLEAN = 1;

    public static int bytesToInteger(byte[] b) {
        int val = 0;

        val += (b[0] & 0xff) << 24;
        val += (b[1] & 0xff) << 16;
        val += (b[2] & 0xff) << 8;
        val += (b[3] & 0xff);

        return val;
    }

    public static byte[] integerToBytes(int val) {
        byte[] b = new byte[4];

        b[0] = (byte) ((val >> 24) & 0xff);
        b[1] = (byte) ((val >> 16) & 0xff);
        b[2] = (byte) ((val >> 8) & 0xff);
        b[3] = (byte) (val & 0xff);

        return b;
    }

    public static boolean byteToBoolean(byte[] b) {
        return (b[0] == 1);
    }

    public static byte[] booleanToByte(boolean val) {
        byte[] b = new byte[1];

        b[0] = (byte) (val ? 1 : 0);

        return b;
    }
}
