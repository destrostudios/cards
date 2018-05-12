package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.GameEventHandler;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class PlayCardFromHandHandler extends GameEventHandler<PlayCardFromHandEvent> {

    @Override
    public void handle(PlayCardFromHandEvent event) {
        events.fireSubEvent(new RemoveCardFromHandEvent(event.card));
        events.fireSubEvent(new AddCardToBoardEvent(event.card));
    }
}
