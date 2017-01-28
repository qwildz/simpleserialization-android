package jaron.simpleserialization;

import android.util.Log;

/**
 * The <code>SerializationSerialReader</code> class is used to analyze a serial
 * data stream and extract the packages that contain the serialized data.
 *
 * @author jarontec gmail com
 * @version 1.0
 * @since 1.0
 */
public class SerializationSerialReader {
    private static final int BUFFER_SIZE = 2048;

    private SerializationData data;
    private byte buffer[] = new byte[BUFFER_SIZE];
    short bufferCounter = 0;
    private boolean doCollectData = false;
    short preambleCounter = 0;
    short postambleCounter = 0;
    private byte[] preamble = new byte[SerializationTypes.SIZEOF_INTEGER];
    private byte[] postamble = new byte[SerializationTypes.SIZEOF_INTEGER];

    public SerializationSerialReader(SerializationData data) {
        this.data = data;
        preamble = SerializationTypes.integerToBytes(data.getPreamble());
        postamble = SerializationTypes.integerToBytes(data.getPostamble());
    }

    public void processData(byte b) {
        if (doCollectData) {
            // if the collect data flag is set (after receiving the preamble) then collect the serial data
            collectData(b);
        } else {
            if (b == preamble[preambleCounter]) {
                // if the data byte is a preamble sequence byte then collect the preamble
                buffer[preambleCounter] = b;
                ++preambleCounter;
                Log.d(this.getClass().toString(), "Correct preamble!");
            } else {
                // if the data byte is not a preamble sequence byte then reset the preamble counter
                preambleCounter = 0;
                Log.d(this.getClass().toString(), "False preamble! " + b + " with " + preamble[preambleCounter]);
            }
            // if the whole preamble sequence is received...
            if (preambleCounter == SerializationTypes.SIZEOF_INTEGER) {
                // reset the preamble counter to be ready to count the next preamble seuquence byte
                preambleCounter = 0;
                // start collecting the data
                bufferCounter = SerializationTypes.SIZEOF_INTEGER - 1; // the preamble is already stored in the buffer
                doCollectData = true;
            } else {
                Log.d(this.getClass().toString(), "Preamble not received!");
            }
        }
    }

    protected void collectData(byte b) {
        // increase the buffer counter but prevent a buffer overflow
        if (bufferCounter < buffer.length) {
            ++bufferCounter;
        }

        // fill the buffer
        buffer[bufferCounter] = b;

        // check for a postamble sequence byte
        if (b != postamble[postambleCounter]) {
            postambleCounter = 0;
        } else {
            ++postambleCounter;
        }
        // if the whole postamble sequence is received...
        if (postambleCounter == SerializationTypes.SIZEOF_INTEGER) {
            // do a checksum test before deserializing the data
            byte checksumBytes[] = new byte[SerializationTypes.SIZEOF_INTEGER];
            for (int i = 0; i < SerializationTypes.SIZEOF_INTEGER; ++i) {
                checksumBytes[i] = buffer[i + SerializationTypes.SIZEOF_INTEGER];
            }
            // deserialize the data and fire an update event
            if (SerializationTypes.bytesToInteger(checksumBytes) == bufferCounter + 1) {
                SerializationInputStream input = new SerializationInputStream(buffer, 0, bufferCounter);
                data.read(input);
                data.notifyListeners();
            } else {
                Log.d(this.getClass().toString(), "SerializationReader: Checksum failed!");
            }
            // reset the postamble counter to be ready to count the next postamble seuquence byte
            postambleCounter = 0;
            // stop collecting the data (start waiting for the preamble sequence)
            doCollectData = false;
        } else {
            Log.d(this.getClass().toString(), "Packet corrupted!");
        }
    }
}
