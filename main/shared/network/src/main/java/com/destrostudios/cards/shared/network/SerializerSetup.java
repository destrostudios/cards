package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.cards.shared.rules.battle.AttackEvent;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.EnumSerializer;

/**
 *
 * @author Philipp
 */
public class SerializerSetup {

    static {
        registerClasses(
                Tuple.class,
                FullGameStateMessage.class,
                    FullGameState.class,
                ClientReadyMessage.class,
                ActionNotificationMessage.class,
                ActionRequestMessage.class,
                    GameStartEvent.class,
                    AttackEvent.class,
                    PlaySpellEvent.class,
                    EndTurnEvent.class
        );
        Serializer.registerClass(Foil.class, new EnumSerializer());
        Serializer.registerClass(ComponentDefinition.class, new ComponentDefinitionSerializer());
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
