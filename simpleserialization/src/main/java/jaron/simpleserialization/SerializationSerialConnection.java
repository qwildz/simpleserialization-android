package jaron.simpleserialization;

import java.util.ArrayList;

/**
 * The <code>SerializationSerialConnection</code> class provides the functionality
 * to establish a serial connection and send and receive serialized data.
 *
 * @author jarontec gmail com
 * @version 1.0
 * @since 1.0
 */
public class SerializationSerialConnection {

    private ArrayList<SerializationSerialReader> readers = new ArrayList<SerializationSerialReader>();

    public SerializationSerialConnection() {

    }

    public void addDeserializableData(SerializationData data) {
        SerializationSerialReader reader = new SerializationSerialReader(data);
        addReader(reader);
    }

    public void addReader(SerializationSerialReader reader) {
        readers.add(reader);
    }

    public void read(byte b) {
        for (SerializationSerialReader reader : readers) {
            reader.processData(b);
        }
    }

    public void read(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            read(b[i]);
        }
    }

    public byte[] write(SerializationData data) {
        SerializationOutputStream output = new SerializationOutputStream(data.getDataSize());
        data.write(output);                 // serialize data and write it to a buffer
        return output.toByteArray();        // write the buffer to the serial interface
    }
}
