package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromBoardHandler extends GameEventHandler<RemoveCardFromBoardEvent> {

    @Override
    public void handle(RemoveCardFromBoardEvent event, NetworkRandom random) {
        if (data.hasComponent(event.card, Components.SPELL_CARD)) {
            events.fire(new RemoveCardFromSpellZoneEvent(event.card), random);
        }
        else if (data.hasComponent(event.card, Components.CREATURE_CARD)) {
            events.fire(new RemoveCardFromCreatureZoneEvent(event.card), random);
        }
        throw new AssertionError("Can't find target zone for " + event.card);
    }
}
