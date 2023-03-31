package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToBoardHandler extends GameEventHandler<AddCardToBoardEvent> {

    @Override
    public void handle(AddCardToBoardEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            events.fire(new AddCardToCreatureZoneEvent(event.card), random);
        } else {
            throw new AssertionError("Can't find target zone for " + event.card);
        }
    }
}
