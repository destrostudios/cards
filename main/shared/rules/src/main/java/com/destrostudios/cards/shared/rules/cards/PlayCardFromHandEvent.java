package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 **/
public class PlayCardFromHandEvent extends Event {
    public int card;

    public PlayCardFromHandEvent(int card) {
        this.card = card;
    }
}
