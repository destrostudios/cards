package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventHandler;
import com.destrostudios.cards.shared.events.EventQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 **/
public class PlayCardFromHandHandler implements EventHandler<PlayCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlayCardFromHandHandler.class);

    private final EntityData data;
    private final EventQueue events;

    public PlayCardFromHandHandler(EntityData data, EventQueue events) {
        this.data = data;
        this.events = events;
    }

    @Override
    public void onEvent(PlayCardFromHandEvent event) {
        events.fireSubevent(new RemoveCardFromHandEvent(event.card));
        events.fireSubevent(new AddCardToBoardEvent(event.card));
    }
}
