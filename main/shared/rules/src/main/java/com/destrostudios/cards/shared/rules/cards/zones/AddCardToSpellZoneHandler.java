package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;

public class AddCardToSpellZoneHandler extends GameEventHandler<AddCardToSpellZoneEvent> {

    @Override
    public void handle(AddCardToSpellZoneEvent event, NetworkRandom random) {
        events.fire(new AddCardToBoardZoneEvent(event.card, Components.SPELL_ZONE), random);
    }
}
