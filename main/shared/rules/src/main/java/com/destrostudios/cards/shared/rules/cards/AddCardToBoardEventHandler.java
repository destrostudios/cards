package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 **/
public class AddCardToBoardEventHandler implements EventHandler<AddCardToBoardEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AddCardToBoardEventHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public AddCardToBoardEventHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(AddCardToBoardEvent event) {
        data.setComponent(event.card, Components.BOARD);
        int playerEntity = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, Components.CREATURE_ZONE, data.entities(Components.CREATURE_ZONE,
                card -> data.getComponent(card, Components.OWNED_BY) == playerEntity).size());
    }
}
