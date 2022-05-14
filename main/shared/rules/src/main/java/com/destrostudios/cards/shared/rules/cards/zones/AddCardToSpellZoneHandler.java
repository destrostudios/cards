package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToSpellZoneHandler extends GameEventHandler<AddCardToSpellZoneEvent> {

    @Override
    public void handle(AddCardToSpellZoneEvent event) {
        events.fire(new AddCardToBoardZoneEvent(event.card, Components.SPELL_ZONE));
    }
}
