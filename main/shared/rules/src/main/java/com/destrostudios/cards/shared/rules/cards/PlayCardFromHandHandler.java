package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import java.util.function.IntUnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class PlayCardFromHandHandler implements GameEventHandler<PlayCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlayCardFromHandHandler.class);

    @Override
    public void handle(EntityData data, EventQueue events, IntUnaryOperator random, PlayCardFromHandEvent event) {
        events.fireSubEvent(new RemoveCardFromHandEvent(event.card));
        events.fireSubEvent(new AddCardToBoardEvent(event.card));
    }
}
