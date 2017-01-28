package jaron.simpleserialization;

import java.util.EventListener;

/**
 * The <code>SerializationDataEventListener</code> interface must be implemented
 * by all the classes that want to be informed about a change of serialized
 * data.
 *
 * @author jarontec gmail com
 * @version 1.0
 * @since 1.0
 */
public interface SerializationDataEventListener extends EventListener {
    public void dataUpdate(SerializationData data);
}
