package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class AddCardToBoardHandler implements GameEventHandler<AddCardToBoardEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AddCardToBoardHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, AddCardToBoardEvent event) {
        data.setComponent(event.card, Components.BOARD);
        int playerEntity = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, Components.CREATURE_ZONE, data.entities(Components.CREATURE_ZONE,
                card -> data.getComponent(card, Components.OWNED_BY) == playerEntity).size());
    }
}
