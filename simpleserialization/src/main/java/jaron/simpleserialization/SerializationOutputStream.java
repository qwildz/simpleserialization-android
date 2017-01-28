package jaron.simpleserialization;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The <code>SerializationOutputStream</code> class implements the functionality
 * to deserialize serial data to simple data types like integers, floats and
 * strings.
 *
 * @author      jarontec gmail com
 * @version     1.0
 * @since       1.0
 */
public class SerializationOutputStream extends ByteArrayOutputStream {
  public SerializationOutputStream(int size) {
    super(size);
  }

  public void writeInteger(int val) {
    byte b[] = SerializationTypes.integerToBytes(val);
    write(b, 0, b.length);
  }

  public void writeFloat(float val) {
    //writeInteger(Float.floatToIntBits(val));
    byte b[] = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(val).array();
    write(b, 0, b.length);
  }

  public void writeString(String str) {
    writeInteger(str.length());
    write(str.getBytes(), 0, str.length());
  }

  public void writeBoolean(boolean val) {
	byte b[] = SerializationTypes.booleanToByte(val);
    write(b, 0, b.length);
  }
}
