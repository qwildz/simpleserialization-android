package jaron.simpleserialization;

/**
 * The <code>SerializationData</code> class is the basis for all the classes
 * that will be serialized.
 *
 * @author      jarontec gmail com
 * @version     1.0
 * @since       1.0
 */
public abstract class SerializationData {
  private int preamble = 123456789;   // default preamble
  private int checksum;               // calculated at runtime
  private int postamble = 987654321;  // default postamble
  private SerializationDataEventListener mlistener = null;

  public int getPreamble() {
    return preamble;
  }

  public void setPreamble(int preamble) {
    this.preamble = preamble;
  }

  public int getPostamble() {
    return postamble;
  }

  public void setPostamble(int postamble) {
    this.postamble = postamble;
  }

  public int getChecksum() {
    return checksum;
  }

  public void setChecksum(int checksum) {
    this.checksum = checksum;
  }

  public int getSize() {
    int size = SerializationTypes.SIZEOF_INTEGER; // preamble
    size += SerializationTypes.SIZEOF_INTEGER;    // checksum
    size += getDataSize();                        // subclass' data size
    size += SerializationTypes.SIZEOF_INTEGER;    // postamble

    return size;
  }

  public void read(SerializationInputStream input) {
    setPreamble(input.readInteger());
    setChecksum(input.readInteger());

    readData(input); // let the subclass read its data

    setPostamble(input.readInteger());
  }

  public void write(SerializationOutputStream output) {
    output.writeInteger(getPreamble());
    output.writeInteger(getSize());

    writeData(output); // let the subclass write its data

    output.writeInteger(getPostamble());
  }

  public void setListener(SerializationDataEventListener listener) {
    mlistener = listener;
  }

  protected synchronized void notifyListeners() {
    if (mlistener != null) {
      mlistener.dataUpdate(this);
    }
  }

  public abstract void readData(SerializationInputStream input);

  public abstract void writeData(SerializationOutputStream output);

  public abstract int getDataSize();
}
