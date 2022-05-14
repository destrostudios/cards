package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class RemoveCardFromSpellZoneHandler extends GameEventHandler<RemoveCardFromSpellZoneEvent> {

    @Override
    public void handle(RemoveCardFromSpellZoneEvent event) {
        events.fire(new RemoveCardFromBoardZoneEvent(event.card, Components.SPELL_ZONE));
    }
}
