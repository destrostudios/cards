package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class PlayCardFromHandHandler extends GameEventHandler<PlayCardFromHandEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(PlayCardFromHandHandler.class);

    @Override
    public void handle(PlayCardFromHandEvent event) {
        events.fireSubEvent(new RemoveCardFromHandEvent(event.card));
        events.fireSubEvent(new AddCardToBoardEvent(event.card));
    }
}
