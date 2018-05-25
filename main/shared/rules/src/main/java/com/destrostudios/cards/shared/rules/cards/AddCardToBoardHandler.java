package com.destrostudios.cards.shared.rules.cards;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

/**
 * Implemented by Gerd-Emmanuel Nandzik
 *
 */
public class AddCardToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    @Override
    public void handle(AddCardToBoardEvent event) {
        data.setComponent(event.card, Components.BOARD);
        int playerEntity = data.getComponent(event.card, Components.OWNED_BY);
        data.setComponent(event.card, Components.CREATURE_ZONE, data.query(Components.CREATURE_ZONE).list(card -> data.getComponent(card, Components.OWNED_BY) == playerEntity).size());
    }
}
