package com.destrostudios.cards.shared.network;

import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Philipp
 */
public class SerializerSetup {

    static {
        Serializer.registerClass(ActionNotificationMessage.class);
        Serializer.registerClass(ActionRequestMessage.class);
    }

    public static void ensureInitialized() {
        //call this method to ensure the static initializer was executed
    }

}
