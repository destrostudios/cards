package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Philipp
 */
public class SerializerSetup {

    static {
        registerClasses(
                ActionNotificationMessage.class,
                ActionRequestMessage.class,
                GameStartEvent.class);
    }

    private static void registerClasses(Class<?>... classes) {
        for (Class<?> classe : classes) {
            //registers the class without need for @Serializable
            Serializer.getSerializerRegistration(classe, false);
        }
    }

    public static void ensureInitialized() {
        //call this method to ensure the static initializer was executed
    }

}
