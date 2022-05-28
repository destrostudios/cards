package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class RemoveCardFromSpellZoneHandler extends GameEventHandler<RemoveCardFromSpellZoneEvent> {

    @Override
    public void handle(RemoveCardFromSpellZoneEvent event, NetworkRandom random) {
        events.fire(new RemoveCardFromBoardZoneEvent(event.card, Components.SPELL_ZONE), random);
    }
}
