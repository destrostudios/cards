package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.events.Event;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 **/
public class AddCardToBoardEvent extends Event {
    final int card;

    public AddCardToBoardEvent(int card) {
        this.card = card;
    }
}
