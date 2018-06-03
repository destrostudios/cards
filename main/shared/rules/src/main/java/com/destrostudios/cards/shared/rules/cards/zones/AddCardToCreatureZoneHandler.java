package com.destrostudios.cards.shared.rules.cards.zones;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameEventHandler;

public class AddCardToCreatureZoneHandler extends GameEventHandler<AddCardToCreatureZoneEvent> {

    @Override
    public void handle(AddCardToCreatureZoneEvent event) {
        events.fireSubEvent(new AddCardToBoardZoneEvent(event.card, Components.CREATURE_ZONE));
    }
}
