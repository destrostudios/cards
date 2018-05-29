package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.network.messages.*;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.phases.attack.EndAttackPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.block.EndBlockPhaseEvent;
import com.destrostudios.cards.shared.rules.game.phases.main.EndMainPhaseEvent;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author Philipp
 */
public class SerializerSetup {

    static {
        registerClasses(Tuple.class,
                FullGameStateMessage.class,
                    FullGameState.class,
                ClientReadyMessage.class,
                ActionNotificationMessage.class,
                ActionRequestMessage.class,
                    BattleEvent.class,
                    DrawCardEvent.class,
                    GameStartEvent.class,
                    PlaySpellEvent.class,
                    EndAttackPhaseEvent.class,
                    EndBlockPhaseEvent.class,
                    EndMainPhaseEvent.class
        );
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
